const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";
//console.log("Script is running...");

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("registrationForm");
  const DriverId = sessionStorage.getItem("driverId");

  if (!DriverId) {
    console.error("Driver ID not found in sessionStorage");
    return;
  }
  //console.log('Driver ID:', DriverId);

  // Fetch vehicle details from the backend
  fetch(`${BASE_URL}/api/vehiclesByDriverId?driverId=${DriverId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((data) => {
      //console.log('Data received from backend:', data);

      // Access the first vehicle in the response array
      const vehicle = data[0];
      //console.log('Vehicle details:', vehicle);

      // Populate form fields with the retrieved data
      form.querySelector('input[name="vehicleModel"]').value =
        vehicle.vehicleModel || "";
      form.querySelector('input[name="yearOfManufacture"]').value =
        vehicle.yearOfManufacture || "";
      form.querySelector('input[name="licensePlateNumber"]').value =
        vehicle.licensePlateNumber || "";
      form.querySelector('input[name="insuranceCompany"]').value =
        vehicle.insuranceCompany || "";
      form.querySelector('input[name="insurancePolicyNumber"]').value =
        vehicle.insurancePolicyNumber || "";
      form.querySelector('input[name="insuranceValidity"]').value =
        vehicle.insuranceValidity || "";
      form.querySelector('input[name="driverName"]').value =
        vehicle.driverName || "";
      form.querySelector('input[name="driverPhone"]').value =
        vehicle.driverPhone || "";
      form.querySelector('input[name="transportName"]').value =
        vehicle.transportName || "";
      form.querySelector('input[name="transportLocation"]').value =
        vehicle.transportLocation || "";
      form.querySelector('input[name="transportPhone"]').value =
        vehicle.transportPhone || "";
      form.querySelector('input[name="fleetSize"]').value =
        vehicle.fleetSize || "";
      form.querySelector('input[name="vehiclePrice"]').value =
        vehicle.vehiclePrice || "";

      // Set all form fields as readonly
      const inputs = form.querySelectorAll("input, select");
      inputs.forEach((input) => {
        input.setAttribute("readonly", true);
      });
    })
    .catch((error) => console.error("Error fetching vehicle details:", error));

  // Prevent form submission after setting the details
  form.addEventListener("submit", function (event) {
    event.preventDefault();
    //console.log('Form submission prevented.');
  });
});

//--------------------------------------------------------------------------------------------------//
function showLoadingPopup(text) {
  document.getElementById("loadingPopup").style.display = "block";
  document.getElementById("loadingText").innerText = text;
}

function hideLoadingPopup() {
  document.getElementById("loadingPopup").style.display = "none";
}
function bookVehicle() {
  var driverId = sessionStorage.getItem("driverId"); // Assuming driverId is stored in session storage
  var cfsEmail = sessionStorage.getItem("signinEmail");
  var citydeparture = sessionStorage.getItem("cityDeparture");
  var citydeliver = sessionStorage.getItem("cityDeliver");

  showLoadingPopup("Booking vehicle. Please wait...");

  var requestData = {
    message: "Do you accept the booking? Please confirm.",
    driverId: driverId,
    cfsEmail: cfsEmail,
    departure: citydeparture,
    deliver: citydeliver,
  };

  fetch(`${BASE_URL}/api/send-alert`, {
    method: "POST",
    body: JSON.stringify(requestData),
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.message === "Notification sent successfully") {
        waitForDriverResponse(); // Wait for the driver's response
      } else {
        alert("Error sending alert to backend: " + data.error);
        hideLoadingPopup();
      }
    })
    .catch((error) => {
      console.error("Error sending alert to backend:", error);
      hideLoadingPopup();
    });
}

function waitForDriverResponse() {
  var driverId = sessionStorage.getItem("driverId");

  // Introduce a delay before making the fetch call
  setTimeout(() => {
    fetch(
      `${BASE_URL}/api/check-response?driverId=${encodeURIComponent(driverId)}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        console.log(data);
        // Check if the status is "Accepted"
        if (data.status === "Accepted") {
          alert("Booking confirmed. Proceeding to next step.");
          window.location.href = "cfs-dashboard.html";
        } else {
          // For any status other than "Accepted", show "Booking not confirmed"
          alert("Booking not confirmed.");
          window.location.href = "ratecard.html";
        }
        hideLoadingPopup();
      })
      .catch((error) => {
        console.error("Error checking driver response:", error);
        // Optionally, you could handle retries or show an error message here
        hideLoadingPopup();
      });
  }, 10000); // Wait for 5 seconds before sending the request
}
