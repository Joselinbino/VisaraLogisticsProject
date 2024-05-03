const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";
//------------------------------------------------------------------------------//
const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get("orderId");

if (!orderId) {
  console.error("Order ID not found in URL");
} else {
  const orderIDElement = document.getElementById("orderID");
  const estimatedDeliveryTimeElement = document.getElementById(
    "estimatedDeliveryTime"
  );
  const shippingByElement = document.getElementById("shippingBy");

  orderIDElement.textContent = `Order ID: ${orderId}`;

  const estimatedDeliveryTime = new Date();
  estimatedDeliveryTime.setDate(estimatedDeliveryTime.getDate() + 7); // Example: Estimated delivery in 7 days
  estimatedDeliveryTimeElement.textContent = `${estimatedDeliveryTime.toDateString()}`;

  fetch(`${BASE_URL}/api/order/${orderId}`)
    .then((response) => response.json())
    .then((data) => {
      const transportName = data.transport.transportName;
      shippingByElement.textContent = `${transportName}`;
    })
    .catch((error) => {
      console.error("Error fetching order details:", error);
    });

  let existingCheckpoint = "";

  async function trackOrder() {
    // Fetch the driver's location (assuming you have an endpoint for this)
    const driverLocationResponse = await fetch(
      `${BASE_URL}/api/driverLocation?orderId=${orderId}`
    );
    const driverLocationData = await driverLocationResponse.json();
    const driverLat = driverLocationData.DriverLat;
    const driverLong = driverLocationData.DriverLong;

    // Fetch the order status
    const orderStatusResponse = await fetch(
      `${BASE_URL}/api/orderStatus?orderId=${orderId}`
    );
    const orderStatusData = await orderStatusResponse.json();
    const orderStatus = orderStatusData.status;

    // Fetch the checkpoints
    const response = await fetch(
      `${BASE_URL}/api/trackOrder?orderId=${orderId}`
    );
    const data = await response.json();
    const trackingInfo = document.getElementById("trackingInfo");
    if (response.ok) {
      const { departure, departureCheckpoint, deliveryCheckpoint, delivery } =
        data;
      const steps = trackingInfo.querySelectorAll(".step");

      // Update the tracking information
      steps[0].querySelector(".text").textContent = "Order confirmed";
      steps[1].querySelector(".text").textContent = `${departure}`;
      steps[2].querySelector(".text").textContent = `${departureCheckpoint}`;
      steps[3].querySelector(".text").textContent = `${deliveryCheckpoint}`;
      steps[4].querySelector(".text").textContent = `${delivery}`;

      // Check order status and update all steps if order is completed
      // Check order status and update all steps if order is completed
      if (orderStatus === "Completed") {
        steps.forEach((step) => {
          step.classList.add("active");
        });
      } else {
        let closestCheckpoint = null;
        let closestDistance = Number.MAX_VALUE;

        // Check distance for each checkpoint and find the closest one
        for (let j = 2; j <= 3; j++) {
          const checkpointName = steps[j].querySelector(".text").textContent;
          const checkpointLocation = await fetchCheckpointLocation(
            checkpointName
          );
          const checkpointLat = checkpointLocation.CheckpointLatitude;
          const checkpointLong = checkpointLocation.CheckpointLongitude;

          const distance = calculateDistance(
            driverLat,
            driverLong,
            checkpointLat,
            checkpointLong
          );
          console.log("Distance to", checkpointName, ":", distance);

          if (distance <= 2.0 && distance < closestDistance) {
            closestCheckpoint = steps[j];
            closestDistance = distance;
          }
        }

        // Add 'active' class to the closest checkpoint
        if (closestCheckpoint) {
          console.log(
            "Closest checkpoint is within 2 km radius:",
            closestCheckpoint.querySelector(".text").textContent
          );
          steps[2].classList.add("active"); // Add active class to departure checkpoint
          steps[3].classList.add("active"); // Add active class to delivery checkpoint
        }
      }
    } else {
      trackingInfo.textContent = data.error || "Failed to track order";
    }
  }

  // Helper function to fetch the location of a checkpoint
  async function fetchCheckpointLocation(checkpointName) {
    try {
      const response = await fetch(
        `${BASE_URL}/api/checkpointLocation?checkpointName=${checkpointName}`
      );
      if (!response.ok) {
        throw new Error("Failed to fetch checkpoint location");
      }
      const data = await response.json(); // Parse the JSON response
      //console.log(data); // Log the parsed data
      return data;
    } catch (error) {
      console.error("Error fetching checkpoint location:", error);
      return null; // Return null or handle the error appropriately
    }
  }

  const R = 6371.0;

  function calculateDistance(lat1, lon1, checkpointLat, checkpointLong) {
    // Convert latitude and longitude from degrees to radians
    const lat1Rad = deg2rad(lat1);
    const lon1Rad = deg2rad(lon1);
    const lat2Rad = deg2rad(checkpointLat);
    const lon2Rad = deg2rad(checkpointLong);

    // Calculate the differences
    const dLat = lat2Rad - lat1Rad;
    const dLon = lon2Rad - lon1Rad;

    // Calculate the distance using the Haversine formula
    const a =
      Math.pow(Math.sin(dLat / 2), 2) +
      Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = R * c;

    //console.log("Distance " + distance);

    return distance;
  }

  function deg2rad(deg) {
    return deg * (Math.PI / 180);
  }

  trackOrder(); // Call the function to track the order when the page loads
}
