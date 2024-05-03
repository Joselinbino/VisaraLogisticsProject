const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";
//-------------------------------------------------------------------------------------//

$(document).ready(function () {
  $('[data-toggle="tooltip"]').tooltip();

  // Function to update hint text
  function updateHintText(numVisibleRows, totalRows) {
    var $hintText = $(".hint-text");
    $hintText.html(
      "Showing <b>" +
        numVisibleRows +
        "</b> out of <b>" +
        totalRows +
        "</b> entries"
    );
  }

  // Function to handle filtering
  function filterTable() {
    var status = $("#filter-status").val().toLowerCase();
    var fromDate = $("#filter-from-date").val();
    var toDate = $("#filter-to-date").val();
    var cfsName = $("#filter-cfs-name").val().toLowerCase(); // New

    //console.log('From Date:', fromDate);
    //console.log('To Date:', toDate);

    $("tbody tr").each(function () {
      var $row = $(this);
      var isVisible = true;

      // Check for status filter
      if (status !== "any") {
        var tableStatus = $row
          .find("td:eq(6)")
          .text()
          .toLowerCase()
          .replace("•", "")
          .trim();
        if (tableStatus !== status.toLowerCase()) {
          isVisible = false;
        }
      }

      // Check for date filter
      var orderDate = new Date($row.find("td:eq(4)").text());
      if (fromDate && toDate) {
        // Remove the time component from the dates
        var orderDateWithoutTime = new Date(
          orderDate.getFullYear(),
          orderDate.getMonth(),
          orderDate.getDate()
        );
        var fromDateWithoutTime = new Date(fromDate);
        var toDateWithoutTime = new Date(toDate);

        //console.log('Order Date:', orderDateWithoutTime);
        //console.log('From Date Without Time:', fromDateWithoutTime);
        //console.log('To Date Without Time:', toDateWithoutTime);

        // Compare the dates without time component
        if (
          orderDateWithoutTime < fromDateWithoutTime ||
          orderDateWithoutTime > toDateWithoutTime
        ) {
          isVisible = false;
        }
      }
      // Check for CFS name filter
      var tableCfsName = $row.find("td:eq(1)").text().toLowerCase(); // Change the index to match the column
      if (cfsName && !tableCfsName.includes(cfsName)) {
        // New
        isVisible = false; // New
      }

      if (isVisible) {
        $row.show();
      } else {
        $row.hide();
      }
    });

    var $visibleRows = $("tbody tr:visible");
    updateHintText($visibleRows.length, $("tbody tr").length);
  }

  // Event listener for filter change
  $("#filter-status, #filter-from-date, #filter-to-date").on(
    "change",
    filterTable
  );

  // Event listener for "Show entries" change
  $("#entries").on("change", function () {
    var entriesPerPage = parseInt($(this).val());
    var currentPage = 1;
    var $visibleRows = $("tbody tr:visible");
    var totalRows = $visibleRows.length;

    var totalPages = Math.ceil(totalRows / entriesPerPage);

    // Update pagination
    var $pagination = $("#pagination");
    $pagination.empty();
    for (var i = 1; i <= totalPages; i++) {
      var $li = $(
        '<li data-page="' +
          i +
          '"><a href="#" class="page-link">' +
          i +
          "</a></li>"
      );
      if (i === currentPage) {
        $li.addClass("active");
      }
      $pagination.append($li);
    }

    // Show/hide rows based on pagination
    var start = (currentPage - 1) * entriesPerPage;
    var end = start + entriesPerPage;
    $visibleRows.hide().slice(start, end).show();

    updateHintText(entriesPerPage, totalRows);
  });

  // Event listener for pagination links
  $("#pagination").on("click", "li", function (e) {
    e.preventDefault();
    var $this = $(this);
    if ($this.hasClass("active")) return;
    var currentPage = parseInt($this.attr("data-page"));
    var entriesPerPage = parseInt($("#entries").val());
    var $visibleRows = $("tbody tr:visible");
    var totalRows = $visibleRows.length;
    var totalPages = Math.ceil(totalRows / entriesPerPage);

    // Update pagination
    var $pagination = $("#pagination");
    $pagination.find(".active").removeClass("active");
    $this.addClass("active");

    // Show/hide rows based on pagination
    var start = (currentPage - 1) * entriesPerPage;
    var end = start + entriesPerPage;
    $visibleRows.hide().slice(start, end).show();

    updateHintText(entriesPerPage, totalRows);
  });

  // Initial table setup
  filterTable();
  $("#entries").val("5").trigger("change"); // Set initial value for "Show entries"

  // Fetch data from API
  var transportEmail = sessionStorage.getItem("signinEmail");
  fetch(`${BASE_URL}/api/transport/${transportEmail}/orders`)
    .then((response) => response.json())
    .then((data) => {
      var cfsNames = [
        ...new Set(data.map((order) => order.cfs.cfsName.toLowerCase())),
      ];

      // Function to filter suggestions based on user input
      function filterSuggestions(input) {
        return cfsNames.filter((name) => name.startsWith(input.toLowerCase()));
      }

      // Function to update suggestion container
      function updateSuggestions(input) {
        var suggestions = filterSuggestions(input);
        var $suggestionContainer = $("#suggestion-container");
        $suggestionContainer.empty(); // Clear previous suggestions
        if (input && suggestions.length > 0) {
          suggestions.forEach(function (suggestion) {
            var $suggestion = $("<div>").text(suggestion);
            $suggestionContainer.append($suggestion);
          });
          $suggestionContainer.show(); // Show the suggestion container
        } else {
          $suggestionContainer.hide(); // Hide the suggestion container if no input or no suggestions
        }
      }

      // Function to filter table based on selected suggestion
      function filterTableByCfsName(cfsName) {
        $("tbody tr").each(function () {
          var $row = $(this);
          var cfsNameInRow = $row.find("td:eq(1)").text().toLowerCase();
          if (cfsNameInRow === cfsName) {
            $row.show();
          } else {
            $row.hide();
          }
        });
      }

      // Event listener for input field
      $("#filter-cfs-name").on("input", function () {
        var input = $(this).val();
        updateSuggestions(input);
      });

      // Event listener for selecting a suggestion
      $(document).on("click", "#suggestion-container div", function () {
        var selectedCfsName = $(this).text();
        $("#filter-cfs-name").val(selectedCfsName); // Populate selected value in search input field
        $("#suggestion-container").hide(); // Hide suggestion container
        filterTableByCfsName(selectedCfsName); // Filter table based on selected value
      });

      // Event listener to hide suggestion container when clicking outside the input box
      $(document).on("click", function (event) {
        var $target = $(event.target);
        if (!$target.closest("#filter-cfs-name").length) {
          $("#suggestion-container").hide();
        }
      });

      // Event listener to hide suggestion container when search input is empty
      $("#filter-cfs-name").on("keyup", function () {
        var input = $(this).val();
        if (!input) {
          $("#suggestion-container").hide();
        }
      });
      var $tbody = $("tbody");
      $tbody.empty(); // Clear existing rows
      // Convert data to array if it's not already an array
      //data = Array.isArray(data) ? data : Array.from(data);
      data.forEach((order) => {
        var $tr = $("<tr>");
        $tr.append("<td>" + order.orderId + "</td>");
        $tr.append("<td>" + order.cfs.cfsName + "</td>");
        $tr.append("<td>" + order.vehicle.licensePlateNumber + "</td>");
        $tr.append("<td>" + order.vehicle.fleetSize + "feet" + "</td>");
        $tr.append(
          "<td>" + new Date(order.orderDate).toLocaleString() + "</td>"
        );
        $tr.append("<td>" + "Rs." + order.vehicle.vehiclePrice + "</td>");
        $tr.append(
          '<td><span class="status ' +
            (order.status === "Completed" ? "text-success" : "text-warning") +
            '">&bull;</span> ' +
            (order.status === "Completed" ? "Delivered" : "Pending") +
            "</td>"
        );
        $tr.append(
          '<td><a href="invoice.html?orderId=' +
            order.orderId +
            '" class="view" title="View Details" data-toggle="tooltip"><i class="material-icons">&#xE5C8;</i></a></td>'
        );
        $tbody.append($tr);
      });
      filterTable(); // Apply filters after updating table
    })
    .catch((error) => console.error("Error fetching data:", error));
});
