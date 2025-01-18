// Attach event listener to the form
document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent default form submission

    // Get form values
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // Prepare the data
    const data = new URLSearchParams();
    data.append("email", email);
    data.append("password", password);

    // Send the POST request
    fetch("http://192.168.127.136:8080/", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: data.toString(),
    })
        .then((response) => {
            if (response.ok) {
                console.log("Form submitted successfully!");
            } else {
                console.error("Error submitting form:", response.statusText);
            }
        })
        .catch((error) => {
            console.error("Network error:", error);
        });
});
