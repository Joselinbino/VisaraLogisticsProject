/*=============== FETCHING DATAS ===============*/

const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";

// Retrieve the "from" location from session storage
var cityDeparture = sessionStorage.getItem("cityDeparture");

// Check if the value is not null or empty
if (cityDeparture) {
  // Set the value to the dropdown
  var locationSelect = document.getElementById("locationSelect");
  // Iterate over each option to find and select the matching one
  Array.from(locationSelect.options).forEach(function (option) {
    if (option.value === cityDeparture) {
      option.selected = true;
    }
  });
}

var cityDeliver = sessionStorage.getItem("cityDeliver");

// Check if the value is not null or empty
if (cityDeliver) {
  // Set the value to the input field
  var toLocationInput = document.getElementById("toLocation");
  toLocationInput.value = cityDeliver;

  // Make the input field non-editable
  toLocationInput.setAttribute("readonly", "readonly");
}
//------------------------------------------------------------------------------------------------//
document.addEventListener("DOMContentLoaded", function () {
  const cityDepartureSelect = document.getElementById("locationSelect");

  // Send fetch request to fetch location names
  fetch(`${BASE_URL}/fetchLocationsname`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to fetch locations");
      }
      return response.json();
    })
    .then((data) => {
      // Append location names as options
      data.forEach((location) => {
        const option = document.createElement("option");
        option.value = location.locationName; // Assuming location has a name field
        option.textContent = location.locationName;
        cityDepartureSelect.appendChild(option);
      });

      // Retrieve the "from" location from session storage
      var cityDeparture = sessionStorage.getItem("cityDeparture");

      // Check if the value is not null or empty
      if (cityDeparture) {
        // Iterate over each option to find and select the matching one
        Array.from(cityDepartureSelect.options).forEach(function (option) {
          if (option.value === cityDeparture) {
            option.selected = true;
          }
        });
      }
    })
    .catch((error) => {
      console.error("Error fetching locations:", error);
    });
});
//--------------------------------------------------------------------------------------------------------//
const email = sessionStorage.getItem("signinEmail");
// Retrieve the fromLocationName from session storage
const fromLocationName = sessionStorage.getItem("cityDeparture");

// Function to fetch available transports and their vehicles
function fetchAndDisplayTransports() {
  fetch(
    `${BASE_URL}/api/transportsAndVehicles?fromLocationName=${fromLocationName}`
  )
    .then((response) => response.json())
    .then((data) => {
      //console.log(data); // Log the data object to inspect its structure

      const transports = data; // No need to map to extract transportIds

      //console.log(transports);

      Promise.all(
        transports.map((transport) =>
          fetch(
            `${BASE_URL}/checkTransportAssociation?transportId=${transport.transportId}&cfsemail=${email}`
          ) // Make request to check association
            .then((response) => response.json())
            .then((associationStatus) => ({
              transportId: transport.transportId,
              transportName: transport.transportName, // Add transportName to the object
              associated: associationStatus, // Store transportId and association status
              vehicleFleetSize: transport.vehicleFleetSize,
              vehiclePrice: transport.vehiclePrice,
              vehicles: transport.vehicles,
            }))
        )
      ).then((associatedTransports) => {
        const associated = associatedTransports.filter(
          (item) => item.associated
        );
        const unassociated = associatedTransports.filter(
          (item) => !item.associated
        );
        populateTransports("associatedRow", "Associated", associated);
        populateTransports("unassociatedRow", "Unassociated", unassociated);
      });
    })
    .catch((error) =>
      console.error("Error fetching available transports:", error)
    );
}

// Store transport IDs that have been added to the table
let addedTransportIds = [];

// Function to populate transports in the table with associated vehicles
function populateTransports(rowId, associationType, transports) {
  const tbody = document.querySelector(`#${rowId} tbody`);
  tbody.innerHTML = ""; // Clear existing content

  if (transports.length === 0) {
    // Add a default row when no vehicles are available
    const defaultRow = document.createElement("tr");
    defaultRow.innerHTML = `
            <td colspan="5" style="text-align: center;background-color: #f2f2f2;">No vehicles available</td>
        `;
    tbody.appendChild(defaultRow);
  } else {
    transports.forEach((transport) => {
      // Check if the transport ID has already been added
      if (!addedTransportIds.includes(transport.transportId)) {
        //console.log('Transport Object:', transports);
        const transportRow = document.createElement("tr");
        transportRow.innerHTML = `
                    <td>${associationType}</td>
                    <td>${transport.transportName}</td>
                    ${generateVehicleCells(transport.vehicles)}
                `;
        tbody.appendChild(transportRow);

        // Add the transport ID to the list of added IDs
        addedTransportIds.push(transport.transportId);
      }
    });
  }
}

function generateVehicleCells(vehicles) {
  let vehicleCells = "";
  for (let i = 0; i < 3; i++) {
    if (i < vehicles.length) {
      const vehicle = vehicles[i];
      vehicleCells += `
                <td>
                    <div class="card" onclick="showDetails('${vehicle.fleetSize}', ${vehicle.vehiclePrice}, '${vehicle.driver.driverId}')">
                        ${vehicle.fleetSize} feet<br>₹${vehicle.vehiclePrice}
                    </div>
                </td>
            `;
    } else {
      vehicleCells += "<td></td>"; // Empty cell if no vehicle is available
    }
  }
  return vehicleCells;
}

function showDetails(fleetSize, vehiclePrice, driverId) {
  //console.log('showdetails', driverId, vehiclePrice, fleetSize);
  // Store driverId in session storage
  sessionStorage.setItem("driverId", driverId);

  // Redirect to vehicle details page
  window.location.href = "vehicle-detials.html";
}

// Call the function to fetch and display transports
fetchAndDisplayTransports();

//---------------------------------------------------------------------------------------------------//
