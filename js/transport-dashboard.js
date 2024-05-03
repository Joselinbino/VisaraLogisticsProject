const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";
//-----------------------------------------------------------------------------------//

$(document).ready(function () {
  // Function to fetch vehicles data and populate the table
  function fetchAndPopulateVehicles(email) {
    $.ajax({
      url: `${BASE_URL}/api/vehicles?email=` + email,
      type: "GET",
      success: function (response) {
        // Clear previous table data
        $("#vehicleTable tbody").empty();

        // Sort vehicles by registration date in descending order
        response.sort(function (a, b) {
          return new Date(b.registrationDate) - new Date(a.registrationDate);
        });

        // Populate table with sorted data
        $.each(response, function (index, vehicle) {
          $("#vehicleTable tbody").append(
            "<tr>" +
              "<td>" +
              vehicle.vehicleModel +
              "</td>" +
              "<td>" +
              vehicle.licensePlateNumber +
              "</td>" +
              "<td>" +
              vehicle.fleetSize +
              " feet</td>" +
              "<td>₹" +
              vehicle.vehiclePrice +
              " /km</td>" +
              "<td>" +
              '<div class="dropdown">' +
              '<a class="btn btn-link font-24 p-0 line-height-1 no-arrow dropdown-toggle" href="#" role="button" data-toggle="dropdown">' +
              '<i class="dw dw-more"></i>' +
              "</a>" +
              '<div class="dropdown-menu dropdown-menu-right dropdown-menu-icon-list">' +
              '<a class="dropdown-item" href="#"><i class="dw dw-eye"></i>View</a>' +
              "</div>" +
              "</div>" +
              "</td>" +
              "</tr>"
          );
        });
        // Update the vehicle count in the card
        var vehicleCount = response.length;
        $(".widget-data .h4").text(vehicleCount);
      },
      error: function (xhr, status, error) {
        // Handle error
        console.error(error);
      },
    });
  }
  // Call fetchAndPopulateVehicles function with the user's email
  var userEmail = sessionStorage.getItem("signinEmail"); // Replace with actual user email
  fetchAndPopulateVehicles(userEmail);
});

//----------------------------------------------------------------------------------------------------------------------//

// Get the CFS email from session storage
const transportEmail = sessionStorage.getItem("signinEmail");

// Make an API request to get the CFS name using the CFS email
fetch(`${BASE_URL}/api/getTransportName?transportEmail=${transportEmail}`)
  .then((response) => response.json())
  .then((data) => {
    // Update the content of the div element with the CFS name
    document.getElementById("Username1").innerText = data.transportName;
    document.getElementById("Username").innerText = data.transportName;
  })
  .catch((error) => {
    console.error("Error:", error);
  });

//------------------------------------------------------------------------------------------------------------------//
