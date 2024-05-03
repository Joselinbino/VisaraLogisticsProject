const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";
//----------------------------------------------------------------------------------//

document.addEventListener("DOMContentLoaded", function () {
  const invoiceNo = Math.floor(1000 + Math.random() * 9000);

  // Get the current date
  const currentDate = new Date();

  // Format the date as DD-MM-YYYY
  const formattedDate = currentDate.toLocaleDateString("en-GB", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });

  // Fetch data from API
  var transportEmail = sessionStorage.getItem("signinEmail");
  fetch(`${BASE_URL}/api/transport/${transportEmail}/allorders`)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      // Populate invoice details
      document.querySelector(
        ".heading"
      ).textContent = `Invoice No.: ${invoiceNo}`;
      document.querySelector(
        ".sub-heading.issued-date"
      ).textContent = `Issued Date: ${formattedDate}`;
      // Assuming data is an array, loop through each object in the array
      data.forEach((item) => {
        // Check if 'cfs' property exists in the current item
        if (item.cfs) {
          // Populate invoice details with 'cfs' data
          document.querySelector(
            ".sub-heading.full-name"
          ).textContent = `Full Name: ${item.cfs.cfsName}`;
          document.querySelector(
            ".sub-heading.address"
          ).textContent = `Address: ${item.cfs.cfsAddress || "Not provided"}`;
          document.querySelector(
            ".sub-heading.phone-number"
          ).textContent = `Phone Number: ${
            item.cfs.cfsPhone || "Not provided"
          }`;
          document.querySelector(
            ".sub-heading.email-address"
          ).textContent = `Email Address: ${
            item.cfs.cfsemail || "Not provided"
          }`;
        } else {
          console.error("CFS data is undefined");
        }
      });

      //document.querySelector('.sub-heading.full-name').textContent = `Full Name: ${data.cfs.cfsName}`;
      //document.querySelector('.sub-heading.address').textContent = `Address: ${data.cfs.address}`;
      //document.querySelector('.sub-heading.phone-number').textContent = `Phone Number: ${data.cfs.phoneNumber}`;
      //document.querySelector('.sub-heading.email-address').textContent = `Email Address: ${data.cfs.cfsemail}`;
      var $tbody = $("tbody");
      $tbody.empty(); // Clear existing rows
      data.forEach((order) => {
        var $tr = $("<tr>");
        $tr.append("<td>" + order.orderId + "</td>");
        $tr.append(
          "<td>" + new Date(order.orderDate).toLocaleString() + "</td>"
        );
        $tr.append("<td>" + order.cfs.cfsName + "</td>");
        $tr.append("<td>" + order.vehicle.licensePlateNumber + "</td>");
        $tr.append("<td>" + order.vehicle.fleetSize + "feet" + "</td>");
        $tr.append("<td>" + "Rs." + order.vehicle.vehiclePrice + "</td>");
        $tbody.append($tr);
      });

      // Calculate tax and grand total
      const taxRate = 0.18; // Assuming tax rate is 18%
      const taxAmount =
        data.reduce((total, order) => total + order.vehicle.vehiclePrice, 0) *
        taxRate;
      const grandTotal =
        data.reduce((total, order) => total + order.vehicle.vehiclePrice, 0) +
        taxAmount;

      // Populate tax and grand total
      $(".tax-amount").text(`Rs.${taxAmount.toFixed(2)}`);
      $(".grand-total-amount").text(`Rs.${grandTotal.toFixed(2)}`);
    })
    .catch((error) => console.error("Error fetching order details:", error));
});
