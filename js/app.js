document.addEventListener("DOMContentLoaded", function() {
    // Load JSON data for names and coffee types
    loadPeople();
    loadCoffees();

    // Handle form submission via submit event
    document.getElementById("dataForm").addEventListener("submit", function(e) {
        e.preventDefault();  // Prevent default form submission
        submitForm();
    });

    // Handle form submission via submit event
    document.getElementById("monthlyStatisticsForm").addEventListener("submit", function(e) {
        e.preventDefault();  // Prevent default form submission
        monthStatistics();
    });
});


/* Function to load people data from the server */
function loadPeople() {
    ajaxRequest("getPeopleList", displayPeople);
}

/* Function to load coffee types from the server */
function loadCoffees() {
    ajaxRequest("getTypesList", displayCoffees);
}


/* Login credentials */
const username = "coffe";
const password = "kafe";


/* AJAX Request Method */ 
function ajaxRequest(command, callback) {
    const url = `http://ajax1.lmsoft.cz/procedure.php?cmd=${command}`;
    const xhttp = new XMLHttpRequest();
    
    xhttp.open("GET", url, true);
    xhttp.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

    xhttp.onreadystatechange = function() {
        if (xhttp.status >= 200 && xhttp.status < 300) {
            const responseData = JSON.parse(xhttp.responseText);
            callback(responseData);
        } else {
            console.log("Chyba při načítání dat: " + xhttp.status);
        }
    };

    xhttp.send();
}


/* Function to display people on the page */
function displayPeople(data) {
    let nameInputsHtml = '';

    for (let key in data) {
        if (data.hasOwnProperty(key)) {
            nameInputsHtml += `
                <div class="name-group">
                    <input type="radio" id="name${key}" name="user" value="${data[key].ID}">
                    <label for="name${key}">${data[key].name}</label>
                </div>
            `;
        }
    }

    document.getElementById("nameInputs").innerHTML = nameInputsHtml;
}

/* Function to display coffee types on the page */
function displayCoffees(data) {
    let coffeeInputsHtml = '';

    for (let key in data) {
        if (data.hasOwnProperty(key)) {
            coffeeInputsHtml += `
                <div class="coffee-group">
                    <label for="coffee${key}">${data[key].typ}</label>
                    <span id="coffeeValue${key}"><b>5</b></span>
                    <input type="range" id="coffee${key}" name="type[]" min="1" max="10" value="5" class="slider" oninput="updateSliderValue(${key})">
                </div>
            `;
        }
    }

    document.getElementById("coffeeInputs").innerHTML = coffeeInputsHtml;
}


/* Update slider value dynamically */
function updateSliderValue(key) {
    const slider = document.getElementById(`coffee${key}`);
    const output = document.getElementById(`coffeeValue${key}`);

    output.textContent = slider.value;
}


/* Display the summary of coffee drinks */
function displaySummary() {
    const xhttp = new XMLHttpRequest();
    const url = "http://ajax1.lmsoft.cz/procedure.php?cmd=getSummaryOfDrinks";

    xhttp.open("GET", url, true);
    xhttp.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

    xhttp.onreadystatechange = function() {
        if (xhttp.status >= 200 && xhttp.status < 300) {
            const data = JSON.parse(xhttp.responseText);
            renderSummary(data);
        } else {
            console.error("Chyba při načítání shrnutí.");
        }
    };

    xhttp.send();
}

/* Render the summary in the HTML */
function renderSummary(data) {
    let summaryHtml = '<div class="summary-container">';

    if (data.length === 0) {
        summaryHtml += '<p>No data available.</p>';
    } else {
        let personName = '';
        
        data.forEach(item => {
            if (personName !== item[2]) {
                if (personName !== '') {
                    summaryHtml += '</ul></div>';  // Close previous person's section
                }

                personName = item[2];  // Update personName

                summaryHtml += `
                    <div class="summary-item">
                        <h3>${personName}</h3>
                        <ul>
                `;
            }
            summaryHtml += `<li>${item[0]}: ${item[1]}</li>`;  // Display coffee and amount
        });

        summaryHtml += '</ul></div>';  // Close the last person's section
    }

    summaryHtml += '</div>';

    document.getElementById("summary").innerHTML = summaryHtml;
}


/* Function to handle form submission */
function submitForm() {
    const form = document.getElementById("dataForm");

    let formData = new FormData(form);

    const xhttp = new XMLHttpRequest();
    const url = "http://ajax1.lmsoft.cz/procedure.php?cmd=saveDrinks";

    xhttp.open("POST", url, true);
    xhttp.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

    xhttp.onload = function() {
        if (xhttp.status >= 200 && xhttp.status < 300) {
            try {
                const data = JSON.parse(xhttp.responseText);

                if (data["msg"] == 1) {
                    alert("Data byla úspěšně odeslána.");

                    displaySummary();  // Show summary after submission
                }
                else {
                    alert("Data se nepovedlo odeslat!")
                }
            } catch (error) {

            }
        } else {
            alert("Při odesílání dat došlo k chybě.");
        }
    };

    xhttp.send(formData);  // Send form data
}


/* Function showing the table with month statistics */
function monthStatistics() {
    const month = document.getElementById("month").value;

    const form = document.getElementById("monthlyStatisticsForm");
    let formData = new FormData(form);

    if ((month < 1) || (month > 12)) {
        alert("Zadaný měsíc není validný!")
    } else {
        const url = `http://ajax1.lmsoft.cz/procedure.php?cmd=getSummaryOfDrinks&month=${month}`;

        const xhttp = new XMLHttpRequest();
        xhttp.open("GET", url, true);
        xhttp.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

        xhttp.onreadystatechange = function() {
            if (xhttp.readyState === 4) {
                if (xhttp.status >= 200 && xhttp.status < 300) {
                    try {
                        const data = JSON.parse(xhttp.responseText);
                        displayMonthStatistics(data);
                    } catch (e) {
                        console.error("Error parsing JSON response: ", e);
                        console.error("Response received: ", xhttp.responseText);
                    }
                } else {
                    console.error("Chyba při načítání shrnutí. Status: " + xhttp.status);
                }
            }
        };
        xhttp.send(formData);
    }
}

/* Function that fills table with data */
function displayMonthStatistics(data) {
    let tableHtml = ``;
    console.log("Table in HTML: " + tableHtml);

    if (data.length === 0) {
        tableHtml += '<p>No data available.</p>';
    } else {
        document.getElementById("statisticsTable").style.display = "flex";

        let rows = ''; 

        rows += `<tr>
                    <th>Osoba</th>
                    <th>Typ kávy</th>
                    <th>Počet káv</th>
                </tr>`;

        data.forEach(item => {
            rows += `<tr>
                        <td>${item[2]}</td>
                        <td>${item[0]}</td>
                        <td>${item[1]}</td>
                     </tr>`;
        });
        tableHtml += rows;
    }

    document.getElementById("statisticsTable").innerHTML = tableHtml;
}