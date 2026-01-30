document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    loadCustomers();
    loadInventoryForJobs();
    setupRevealAnimations();
    setupTextReveal();
    setupParallax();
    handleQueryParams();
});

// Setup Scroll-triggered Text Reveal
function setupTextReveal() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('active');
            }
        });
    }, { threshold: 0.2 });

    document.querySelectorAll('.reveal-text').forEach(el => observer.observe(el));
}

// Simple Parallax Effect
function setupParallax() {
    window.addEventListener('scroll', () => {
        const scrolled = window.scrollY;
        document.querySelectorAll('.parallax').forEach(el => {
            const speed = el.dataset.speed || 0.2;
            el.style.transform = `translateY(${scrolled * speed}px)`;
        });
    });
}

// Handle auto-searching vehicle if reg param is present
function handleQueryParams() {
    const params = new URLSearchParams(window.location.search);
    const reg = params.get('reg');
    if (reg) {
        const searchInput = document.getElementById('searchReg');
        if (searchInput) {
            searchInput.value = reg;
            // Trigger search
            searchVehicle();
        }
    }
}

// Check if user is logged in
function checkAuth() {
    let user = null;
    try {
        const userData = localStorage.getItem('user');
        console.log("Checking Auth. User in storage:", userData);
        if (userData) {
            user = JSON.parse(userData);
        }
    } catch (e) {
        console.error("Session parse error", e);
        localStorage.removeItem('user');
    }

    const path = window.location.pathname;
    const isLoginPage = path === '/' || path === '/login' || path.endsWith('login.html');

    console.log("Current path:", path, "isLoginPage:", isLoginPage);

    if (!user && !isLoginPage) {
        console.log("No user found, redirecting to login.");
        window.location.href = '/login';
        return;
    }

    if (user) {
        // Role-based Access Control
        if (path === '/dashboard' && user.role !== 'ADMIN') {
            console.log("Access denied. Admin role required for Dashboard.");
            window.location.href = '/home';
            return;
        }

        // Redirection for logged-in users visiting login page
        if (isLoginPage) {
            console.log("User already logged in, redirecting from login page.");
            window.location.href = user.role === 'ADMIN' ? '/dashboard' : '/home';
            return;
        }

        // Add logout button to nav if it doesn't exist
        const nav = document.querySelector('nav');
        if (nav && !document.getElementById('logoutBtn')) {
            const logoutSpan = document.createElement('span');
            logoutSpan.style.marginLeft = '1rem';
            logoutSpan.innerHTML = `<button id="logoutBtn" style="font-size: 0.75rem; padding: 0.3rem 0.6rem; background: #ef4444; color: white; border: none; border-radius: 4px; cursor: pointer;">Logout (${user.username || 'User'})</button>`;
            nav.appendChild(logoutSpan);
            document.getElementById('logoutBtn').addEventListener('click', () => {
                localStorage.removeItem('user');
                window.location.href = '/login';
            });
        }
    }
}

// Setup Scroll Reveal Animations
function setupRevealAnimations() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('active');
            }
        });
    }, { threshold: 0.1 });

    document.querySelectorAll('.reveal').forEach(el => observer.observe(el));
}

// Load customers for dropdown
async function loadCustomers() {
    const select = document.getElementById('customerSelect');
    if (!select) return;

    try {
        const res = await fetch('/api/customers');
        const customers = await res.json();
        select.innerHTML = '<option value="">Choose a customer...</option>';
        customers.forEach(c => {
            const opt = document.createElement('option');
            opt.value = c.id;
            opt.textContent = `${c.name} (${c.phone})`;
            select.appendChild(opt);
        });
    } catch (e) {
        console.error("Error loading customers", e);
    }
}

// Handle Customer Registration
const customerForm = document.getElementById('customerForm');
if (customerForm) {
    customerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            name: document.getElementById('customerName').value,
            email: document.getElementById('customerEmail').value,
            phone: document.getElementById('customerPhone').value,
            address: document.getElementById('customerAddress').value
        };

        const res = await fetch('/api/customers', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert('Customer Registered!');
            customerForm.reset();
            loadCustomers();
        } else {
            alert('Error registering customer');
        }
    });
}

// Handle Vehicle Addition
const vehicleForm = document.getElementById('vehicleForm');
if (vehicleForm) {
    vehicleForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const customerId = document.getElementById('customerSelect').value;
        const data = {
            registrationNumber: document.getElementById('regNumber').value,
            brand: document.getElementById('brand').value,
            model: document.getElementById('model').value
        };

        const res = await fetch(`/api/customers/${customerId}/vehicles`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert('Vehicle Added!');
            vehicleForm.reset();
        } else {
            alert('Error adding vehicle');
        }
    });
}

// --- Service & Repair Module Logic ---

let inventoryParts = [];

async function loadInventoryForJobs() {
    try {
        const res = await fetch('/api/inventory');
        inventoryParts = await res.json();
        updatePartDropdowns();
    } catch (e) {
        console.error("Error loading inventory for jobs", e);
    }
}

function updatePartDropdowns() {
    document.querySelectorAll('.part-select').forEach(select => {
        const currentValue = select.value;
        select.innerHTML = '<option value="">Select Part</option>';
        inventoryParts.forEach(part => {
            const opt = document.createElement('option');
            opt.value = part.id;
            opt.textContent = `${part.name} ($${part.price})`;
            opt.dataset.price = part.price;
            opt.dataset.name = part.name;
            select.appendChild(opt);
        });
        select.value = currentValue;
    });
}

const findVehicleBtn = document.getElementById('findVehicleBtn');
if (findVehicleBtn) {
    findVehicleBtn.addEventListener('click', async () => {
        const reg = document.getElementById('searchVehicle').value;
        if (!reg) return alert('Enter Reg Number');

        try {
            const res = await fetch(`/api/job-cards/search-vehicle?regNumber=${reg}`);
            if (res.ok) {
                const vehicle = await res.json();
                document.getElementById('vehicleDetails').style.display = 'block';
                document.getElementById('foundVehicleInfo').textContent = `${vehicle.brand} ${vehicle.model} - owned by ${vehicle.customer.name}`;
                document.getElementById('selectedVehicleId').value = vehicle.id;
            } else {
                alert('Vehicle not found');
            }
        } catch (e) {
            alert('Error finding vehicle');
        }
    });
}

const addPartBtn = document.getElementById('addPartBtn');
if (addPartBtn) {
    addPartBtn.addEventListener('click', () => {
        const container = document.getElementById('partsContainer');
        const firstRow = container.querySelector('.part-row');
        const newRow = firstRow.cloneNode(true);
        newRow.querySelector('.part-qty').value = 1;
        newRow.querySelector('.part-cost').value = '';
        newRow.querySelector('.part-select').value = '';
        container.appendChild(newRow);
        attachPartListeners(newRow);
    });
}

function attachPartListeners(row) {
    const select = row.querySelector('.part-select');
    const qtyInput = row.querySelector('.part-qty');
    const removeBtn = row.querySelector('.remove-part');

    const updateCost = () => {
        const price = select.options[select.selectedIndex]?.dataset.price || 0;
        const qty = qtyInput.value || 0;
        row.querySelector('.part-cost').value = (price * qty).toFixed(2);
        calculateTotal();
    };

    select.addEventListener('change', updateCost);
    qtyInput.addEventListener('input', updateCost);
    removeBtn.addEventListener('click', () => {
        if (document.querySelectorAll('.part-row').length > 1) {
            row.remove();
            calculateTotal();
        }
    });
}

// Attach to initial row
document.querySelectorAll('.part-row').forEach(row => attachPartListeners(row));

function calculateTotal() {
    let total = 0;
    document.querySelectorAll('.part-cost').forEach(input => {
        total += parseFloat(input.value || 0);
    });
    document.getElementById('estimatedTotal').textContent = `$${total.toFixed(2)}`;
}

const jobCardForm = document.getElementById('jobCardForm');
if (jobCardForm) {
    jobCardForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const vehicleId = document.getElementById('selectedVehicleId').value;

        const parts = [];
        document.querySelectorAll('.part-row').forEach(row => {
            const select = row.querySelector('.part-select');
            if (select.value) {
                parts.push({
                    partName: select.options[select.selectedIndex].dataset.name,
                    quantity: parseInt(row.querySelector('.part-qty').value),
                    unitCost: parseFloat(select.options[select.selectedIndex].dataset.price)
                });
            }
        });

        const data = {
            complaint: document.getElementById('complaint').value,
            serviceType: document.getElementById('serviceType').value,
            mechanicName: document.getElementById('mechanicName')?.value || '',
            partsUsed: parts
        };

        const res = await fetch(`/api/job-cards?vehicleId=${vehicleId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            const job = await res.json();
            alert('Service Job Created Successfully!');
            if (confirm('Do you want to generate a bill summary now?')) {
                generateBill(job);
            }
            jobCardForm.reset();
            document.getElementById('vehicleDetails').style.display = 'none';
        } else {
            alert('Error creating job');
        }
    });
}

function generateBill(job) {
    let billText = `--- SERVICE BILL ---\n`;
    billText += `Vehicle: ${job.vehicle.registrationNumber}\n`;
    billText += `Service: ${job.serviceType}\n`;
    billText += `Mechanic: ${job.mechanicName || 'N/A'}\n`;
    billText += `Date: ${new Date(job.entryDate).toLocaleDateString()}\n\n`;
    billText += `PARTS:\n`;
    job.partsUsed.forEach(p => {
        billText += `- ${p.partName} (x${p.quantity}): $${p.totalCost.toFixed(2)}\n`;
    });
    billText += `\nTOTAL COST: $${job.totalCost.toFixed(2)}\n`;
    billText += `--------------------`;

    const win = window.open('', 'Bill', 'width=400,height=500');
    win.document.write(`<pre>${billText}</pre>`);
    win.document.write(`<button onclick="window.print()">Print Bill</button>`);
}
