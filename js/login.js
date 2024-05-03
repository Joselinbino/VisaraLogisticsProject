
const BASE_URL = "https://visaratransportationproject.uc.r.appspot.com";
//------------------------------------------------------------------------------------------//
$(document).ready(function () {
  $(".switchSignin").click(function () {
    $(".signin-form").removeClass("display-none");
    $(".signin-form").addClass("display-block");
    $(".signup-form").addClass("display-none");
    $(".switchSignin").addClass("btnActive");
    $(".switchSignup").removeClass("btnActive");
  });

  $(".switchSignup").click(function () {
    $(".signup-form").removeClass("display-none");
    $(".signup-form").addClass("display-block");
    $(".signin-form").addClass("display-none");
    $(".switchSignup").addClass("btnActive");
    $(".switchSignin").removeClass("btnActive");
  });

  $("#signinPassIcon").click(function () {
    $("#signinPassIcon i").toggleClass("fa-eye fa-eye-slash");

    $($("#signinPassIcon").siblings()[1]).attr("type", function (index, attr) {
      return attr == "password" ? "text" : "password";
    });
  });

  $("#signupPassIcon").click(function () {
    $("#signupPassIcon i").toggleClass("fa-eye fa-eye-slash");

    $($("#signupPassIcon").siblings()[1]).attr("type", function (index, attr) {
      return attr == "password" ? "text" : "password";
    });
  });
});

/*------------------------------------------------------SIGNUP--------------------------------------------------------------------------*/
document
  .getElementById("btn-signup")
  .addEventListener("click", async function (event) {
    event.preventDefault();
 
    const userType = document.getElementById("userType").value;
    const email = document.getElementById("signupEmail").value;
    const username = document.getElementById("signupUsername").value;
    const password = document.getElementById("signupPassword").value;

    const userData = {
      userType: userType,
      email: email,
      username: username,
      password: password,
    };

    try {
      const response = await fetch(`${BASE_URL}/api/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      const data = await response.json();

      // Check if registration was successful
      if (response.ok) {
        alert("Registration successful!");
        // Clear the form fields
        document.getElementById("userType").value = "";
        document.getElementById("signupEmail").value = "";
        document.getElementById("signupUsername").value = "";
        document.getElementById("signupPassword").value = "";
      } else {
        // Registration failed, display the error message
        alert("Registration failed: " + data.error);
      }
    } catch (error) {
      console.error("Error:", error);
      alert("An error occurred during registration.");
    }
  });

/*---------------------------------------------------------SIGN IN-----------------------------------------------------------------------*/

document.addEventListener("DOMContentLoaded", function () {
  const signInForm = document.querySelector(".signin-form");
  

  signInForm.addEventListener("submit", function (event) {
    event.preventDefault();

    const email = document.getElementById("signinEmail").value;
    const password = document.getElementById("signinPassword").value;

    sessionStorage.setItem("signinEmail", email);

    const loginData = {
      email: email,
      password: password,
    };
   

    fetch(`${BASE_URL}/api/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginData),
    })
      .then((response) => {
        //console.log('API Response:', response);
        return response.json();
      })
      .then((data) => {
        //console.log('Response Data:', data);
        if (data.success) {
          if (data.userType === "Transport") {
            window.location.href = "/transport-dashboard.html";
          } else if (data.userType === "Container Freight Station") {
            window.location.href = "/cfs-dashboard.html";
          } else {
            window.location.href = "/400.html";
          }
        } else {
          alert("Login failed: " + data.error);
        }
        localStorage.clear();
      })
      .catch((error) => {
        console.error("Error:", error);
        alert("Error:", error.message);
      });
  });
});
