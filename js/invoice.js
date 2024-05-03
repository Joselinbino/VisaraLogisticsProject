const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";

//---------------------------------------------------------------------------//

// Get the order ID from the URL query parameter
const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get("orderId");

// Generate a random 4-digit number
const invoiceNo = Math.floor(1000 + Math.random() * 9000);

// Get the current date
const currentDate = new Date();

// Format the date as DD-MM-YYYY
const formattedDate = currentDate.toLocaleDateString("en-GB", {
  day: "2-digit",
  month: "2-digit",
  year: "numeric",
});

// Use the orderId to fetch details about the order
fetch(`${BASE_URL}/api/order/${orderId}`)
  .then((response) => response.json())
  .then((data) => {
    // Populate invoice details
    document.querySelector(
      ".heading"
    ).textContent = `Invoice No.: ${invoiceNo}`;
    document.querySelector(
      ".sub-heading.order-no"
    ).textContent = `Order No: ${data.orderId}`;
    document.querySelector(
      ".sub-heading.issued-date"
    ).textContent = `Issued Date: ${formattedDate}`;
    document.querySelector(
      ".sub-heading.booking-date"
    ).textContent = `Booking Date: ${new Date(
      data.orderDate
    ).toLocaleDateString()}`;
    document.querySelector(
      ".sub-heading.full-name"
    ).textContent = `Full Name: ${data.cfs.cfsName}`;
    document.querySelector(
      ".sub-heading.address"
    ).textContent = `Address: ${data.address}`;
    document.querySelector(
      ".sub-heading.phone-number"
    ).textContent = `Phone Number: ${data.phoneNumber}`;
    document.querySelector(
      ".sub-heading.email-address"
    ).textContent = `Email Address: ${data.cfs.cfsemail}`;

    // Populate order details
    document.querySelector(".order-details .transport-name").textContent =
      data.transport.transportName;
    document.querySelector(".order-details .vehicle-no").textContent =
      data.vehicle.licensePlateNumber;
    document.querySelector(".order-details .fleet-size").textContent =
      data.vehicle.fleetSize + "feet";
    document.querySelector(".order-details .price").textContent =
      "Rs." + data.vehicle.vehiclePrice + ".00";

    // Calculate tax and grand total
    const taxRate = 0.18; // Assuming tax rate is 18%
    const taxAmount = data.vehicle.vehiclePrice * taxRate;
    const grandTotal = data.vehicle.vehiclePrice + taxAmount;

    // Populate tax and grand total

    document.querySelector(".tax").textContent = `Tax(Rate) 18%`;
    document.querySelector(".tax").setAttribute("colspan", "3");
    document.querySelector(".tax-amount").textContent = `Rs.${taxAmount.toFixed(
      2
    )}`;

    document.querySelector(".grand-total").textContent = `Grand Total`;
    document.querySelector(".grand-total").setAttribute("colspan", "3");
    document.querySelector(
      ".grand-total-amount"
    ).textContent = `Rs.${grandTotal.toFixed(2)}`;
  })
  .catch((error) => console.error("Error fetching order details:", error));
