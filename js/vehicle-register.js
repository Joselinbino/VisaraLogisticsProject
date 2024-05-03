const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";
//--------------------------------------------------------------------------------------------------//
document
  .getElementById("registrationForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    var formData = new FormData(this);

    // Retrieve the email from session storage
    var email = sessionStorage.getItem("signinEmail");

    // Check if email is present in session storage
    if (email) {
      // Append the email to the FormData object
      formData.append("email", email);

      fetch(`${BASE_URL}/api/vehicleRegistration`, {
        method: "POST",
        body: formData,
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          return response.text(); // Read response as text
        })
        .then((data) => {
          console.log("Success:", data); // Log the response text
          alert("Success: " + data); // Display response text in alert
          const form = document.getElementById("registrationForm"); // Select the form by its ID
          form.reset(); // Reset the form
        })
        .catch((error) => {
          console.error("Error:", error);
          alert("Error: " + error.message); // Display error message in alert
        });
    } else {
      console.error("Email not found in session storage");
      alert("Email not found in session storage");
    }
  });
