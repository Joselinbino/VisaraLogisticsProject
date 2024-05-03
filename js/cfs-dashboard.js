const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";

// Function to toggle the visibility of the popup div
function togglePopup() {
  var popup = document.getElementById("myPopup");
  if (popup.style.display === "block") {
    popup.style.display = "none";
  } else {
    popup.style.display = "block";
  }
}

// Function to close the modal
function closeModal() {
  document.getElementById("myPopup").style.display = "none";
}

//--------------------------------------------------------------------------------------------//
/*function redirectToPage(event) {
	event.preventDefault(); // Prevent the default form submission behavior
	// Redirect to the desired page
	window.location.href = "ratecard.html";
}*/

//--------------------------------------------------------------------------------------------//
$(document).ready(function () {
  // Event listener for opening the transport modal
  $("#openTransportModal").click(function (event) {
    event.preventDefault(); // Prevent default link behavior
    openTransportModal();
  });

  // Function to open the transport modal
  function openTransportModal() {
    // Fetch the email from session storage
    var userEmail = sessionStorage.getItem("signinEmail");

    if (!userEmail) {
      console.error("User email not found in session storage.");
      return;
    }

    // Fetch transports associated with the CFS from the backend
    $.ajax({
      url: `${BASE_URL}/api/transports?cfsemail=` + userEmail,
      type: "GET",
      success: function (data) {
        // Populate the transport list in the modal
        if (data && data.length > 0) {
          var transportList = $("#transportList");
          transportList.empty(); // Clear previous content
          $.each(data, function (index, transport) {
            // Append a checkbox for each transport
            var checkbox = $(
              '<input type="checkbox" name="transportId" value="' +
                transport.transportId +
                '">'
            );
            // Check if the transport is associated with the CFS
            if (transport.associated) {
              checkbox.prop("checked", true); // Check the checkbox
              checkbox.prop("disabled", true); // Disable the checkbox
            }
            // Append the checkbox and transport name to the list
            transportList
              .append("<div>")
              .append(checkbox)
              .append(" " + transport.transportName);
          });
        } else {
          $("#transportList").html("No transports available.");
        }
      },
      error: function (xhr, status, error) {
        console.error("Error fetching transports:", error);
        $("#transportList").html(
          "Error fetching transports. Please try again later."
        );
      },
    });

    // Open the modal
    $("#transportModal").modal("show");
  }

  $("#transportForm").submit(function (event) {
    event.preventDefault();
    var selectedTransports = [];

    // Retrieve the user's email from session storage
    var userEmail = sessionStorage.getItem("signinEmail");
    if (!userEmail) {
      console.error("User email not found in session storage");
      return; // Handle this case appropriately, maybe prompt the user to log in
    }

    // Iterate over checked transports and push their IDs to selectedTransports array
    $('input[name="transportId"]:checked').each(function () {
      selectedTransports.push($(this).val());
    });

    if (selectedTransports.length === 0) {
      console.error("No transports selected");
      return; // Handle this case appropriately, maybe display a message to the user
    }

    // Send selected transports and user email to the backend for processing
    var requestData = {
      cfsemail: userEmail, // Ensure this matches the backend's expected parameter name
      selectedTransports: selectedTransports,
    };

    // Implement AJAX request to update the database with selected transports and user email
    $.ajax({
      url: `${BASE_URL}/update-transport-cfs`,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(requestData),
      success: function (response) {
        alert(response);
      },
      error: function (error) {
        alert(Response);
      },
      complete: function () {
        // Close the modal after the request is complete, regardless of success or failure
        $("#transportModal").modal("hide");
      },
    });
  });
});
 
//-------------------------------------------------FETCH USERNAME AND SET-----------------------------------------------------//

// Get the CFS email from session storage
const cfsEmail = sessionStorage.getItem("signinEmail");

// Make an API request to get the CFS name using the CFS email
fetch(`${BASE_URL}/getCfsName?cfsEmail=${cfsEmail}`)
  .then((response) => response.json())
  .then((data) => {
    // Update the content of the div element with the CFS name
    document.getElementById("Username").innerText = data.cfsName;
    document.getElementById("Username1").innerText = data.cfsName;
  })
  .catch((error) => {
    console.error("Error:", error);
  });

//------------------------------------------------------------------------------------------//
document.addEventListener("DOMContentLoaded", function () {
  var cfsEmail = sessionStorage.getItem("signinEmail");
  fetch(`${BASE_URL}/cfs/${cfsEmail}/orders`)
    .then((response) => response.json())
    .then((data) => {
      // Sort the orders by order date in descending order
      data.sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate));

      data.forEach((order) => {
        var transportName = order.transport
          ? order.transport.transportName
          : "";
        var fleetSize = order.vehicle ? order.vehicle.fleetSize : "";
        var vehicleNo = order.vehicle ? order.vehicle.licensePlateNumber : "";
        var orderDate = new Date(order.orderDate).toLocaleString(); // Convert timestamp to human-readable date and time

        // Check if fleetSize is not empty before adding ' feet'
        var fleetSizeDisplay = fleetSize ? fleetSize + " feet" : "";
        var statusText = order.status === "Completed" ? "Delivered" : "Pending";

        var row =
          "<tr>" +
          "<td>" +
          order.orderId +
          "</td>" +
          "<td>" +
          transportName +
          "</td>" +
          "<td>" +
          orderDate +
          "</td>" +
          "<td>" +
          vehicleNo +
          "</td>" +
          "<td>" +
          fleetSizeDisplay +
          "</td>" +
          "<td>" +
          statusText +
          "</htd>" +
          //'<td><button class="btn btn-primary">Action</button></td>' +
          "</tr>";
        document
          .querySelector(".data-table tbody")
          .insertAdjacentHTML("beforeend", row);
        0;
      });
    })
    .catch((error) => console.error("Error:", error));
});
//------------------------------------------------------------------------------------------------------------------//
function storeFormData(event) {
	event.preventDefault(); // Prevent form submission

	// Get the selected values from the dropdowns
	var cityDeparture = document.getElementById('cityDeparture').value;
	var cityDeliver = document.getElementById('cityDeliver').value;

	// Store the selected values in session storage
	sessionStorage.setItem('cityDeparture', cityDeparture);
	sessionStorage.setItem('cityDeliver', cityDeliver);

	// Perform the desired action
	// For example, redirect to another page
	window.location.href = 'ratecard.html';
}
//------------------------------------------------------------------------------------------------------------//

document.addEventListener("DOMContentLoaded", function () {
	const cityDeliverSelect = document.getElementById('cityDeliver');
	
	// Get the sign-in mail from session storage
	const cfsEmail = sessionStorage.getItem('signinEmail');

	// Add default "None" option
	//const defaultOption = document.createElement('option');
	//defaultOption.value = "";
	//defaultOption.textContent = "None";
	//cityDeliverSelect.appendChild(defaultOption);

	// Send fetch request to fetch cfs names
	fetch(`${BASE_URL}/getCfsName?cfsEmail=${cfsEmail}`)
	.then(response => {
		if (!response.ok) {
			throw new Error('Failed to fetch cfs name');
		}
		return response.json();
	})
	.then(data => {
		// Clear existing options
		// cityDeliverSelect.innerHTML = '';

		// Append cfs name as option
		const option = document.createElement('option');
		option.value = data.cfsName; // Assuming the API returns an object with a cfsName field
		option.textContent = data.cfsName;
		cityDeliverSelect.appendChild(option);
	})
	.catch(error => {
		console.error('Error fetching cfs name:', error);
	});
});
//------------------------------------------------------------------------------------------------------//
document.addEventListener("DOMContentLoaded", function () {
	const cityDepartureSelect = document.getElementById('cityDeparture');

	// Add default "None" option
	const defaultOption = document.createElement('option');
	defaultOption.value = "";
	defaultOption.textContent = "None";
	cityDepartureSelect.appendChild(defaultOption);

	// Send fetch request to fetch location names
	fetch(`${BASE_URL}/fetchLocationsname`)
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to fetch locations');
			}
			return response.json();
		})
		.then(data => {
			// Append location names as options
			data.forEach(location => {
				const option = document.createElement('option');
				option.value = location.locationName; // Assuming location has a name field
				option.textContent = location.locationName;
				cityDepartureSelect.appendChild(option);
			});
		})
		.catch(error => {
			console.error('Error fetching locations:', error);
		});
});
//---------------------------------------------------------------------------------------------------------------------//
function trackOrder() {
	// Show the modal
	$('#trackOrderModal').modal('show');
}

function submitOrderNumber() {
	// Get the order number from the input field
	let orderId = document.getElementById('orderNumberInput').value;

	// Construct the URL with the order number as a parameter
	let url = 'tracking.html?orderId=' + encodeURIComponent(orderId);

	// Redirect the user to the new page
	window.location.href = url;
}
//----------------------------------------------------------------------------------------------------------//