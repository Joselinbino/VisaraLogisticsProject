const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";
//---------------------------------------------------------------------------------------------//
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
  var cfsEmail = sessionStorage.getItem("signinEmail");
  fetch(`${BASE_URL}/cfs/${cfsEmail}/orders`)
    .then((response) => response.json())
    .then((data) => {
      var $tbody = $("tbody");
      $tbody.empty(); // Clear existing rows
      data.forEach((order) => {
        var $tr = $("<tr>");
        $tr.append("<td>" + order.orderId + "</td>");
        $tr.append("<td>" + order.transport.transportName + "</td>");
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
