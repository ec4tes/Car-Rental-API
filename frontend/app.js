const STORAGE_KEY = "car-rent-api-base";
const DEFAULT_API_BASE = "http://localhost:8080/api/v1";

const state = {
    apiBase: localStorage.getItem(STORAGE_KEY) || DEFAULT_API_BASE,
    brands: [],
    categories: [],
    cars: [],
    customers: [],
    rentals: []
};

const endpoints = {
    brands: {
        list: "/brands/all",
        create: "/brands/create",
        update: (id) => `/brands/update/${id}`,
        delete: (id) => `/brands/delete/${id}`
    },
    categories: {
        list: "/categories/all",
        create: "/categories/create",
        update: (id) => `/categories/update/${id}`,
        delete: (id) => `/categories/delete/${id}`
    },
    cars: {
        list: "/cars",
        create: "/cars",
        update: (id) => `/cars/${id}`,
        delete: (id) => `/cars/${id}`
    },
    customers: {
        list: "/customers/all",
        create: "/customers/create",
        update: (id) => `/customers/update/${id}`,
        delete: (id) => `/customers/delete/${id}`
    },
    rentals: {
        list: "/rentals",
        create: "/rentals",
        complete: (id) => `/rentals/${id}/complete`,
        cancel: (id) => `/rentals/${id}/cancel`
    }
};

const formResetters = {
    brands: resetBrandForm,
    categories: resetCategoryForm,
    cars: resetCarForm,
    customers: resetCustomerForm,
    rentals: resetRentalForm
};

document.addEventListener("DOMContentLoaded", () => {
    bindEvents();
    initializePage().catch(handleError);
});

function bindEvents() {
    document.getElementById("apiBase").value = state.apiBase;
    document.getElementById("swaggerLink").href = state.apiBase.replace("/api/v1", "/swagger-ui.html");

    document.getElementById("saveApiBase").addEventListener("click", async () => {
        const nextValue = document.getElementById("apiBase").value.trim();
        state.apiBase = nextValue || DEFAULT_API_BASE;
        localStorage.setItem(STORAGE_KEY, state.apiBase);
        document.getElementById("swaggerLink").href = state.apiBase.replace("/api/v1", "/swagger-ui.html");
        await refreshAllData();
    });

    document.getElementById("refreshAll").addEventListener("click", refreshAllData);

    document.getElementById("brandForm").addEventListener("submit", submitBrandForm);
    document.getElementById("categoryForm").addEventListener("submit", submitCategoryForm);
    document.getElementById("carForm").addEventListener("submit", submitCarForm);
    document.getElementById("customerForm").addEventListener("submit", submitCustomerForm);
    document.getElementById("rentalForm").addEventListener("submit", submitRentalForm);

    document.getElementById("resetBrandForm").addEventListener("click", resetBrandForm);
    document.getElementById("resetCategoryForm").addEventListener("click", resetCategoryForm);
    document.getElementById("resetCarForm").addEventListener("click", resetCarForm);
    document.getElementById("resetCustomerForm").addEventListener("click", resetCustomerForm);
    document.getElementById("resetRentalForm").addEventListener("click", resetRentalForm);

    document.getElementById("brandTable").addEventListener("click", handleBrandAction);
    document.getElementById("categoryTable").addEventListener("click", handleCategoryAction);
    document.getElementById("carTable").addEventListener("click", handleCarAction);
    document.getElementById("customerTable").addEventListener("click", handleCustomerAction);
    document.getElementById("rentalTable").addEventListener("click", handleRentalAction);
}

async function initializePage() {
    setStatus("Veriler yukleniyor...", "neutral");
    seedRentalDates();
    await refreshAllData();
}

async function refreshAllData() {
    try {
        setStatus("API verileri yenileniyor...", "neutral");
        const [brands, categories, cars, customers, rentals] = await Promise.all([
            request(endpoints.brands.list),
            request(endpoints.categories.list),
            request(endpoints.cars.list),
            request(endpoints.customers.list),
            request(endpoints.rentals.list)
        ]);

        state.brands = brands;
        state.categories = categories;
        state.cars = cars;
        state.customers = customers;
        state.rentals = rentals;

        renderAll();
        setStatus("Baglanti hazir", "success");
        showToast("Tum veriler guncellendi.");
    } catch (error) {
        handleError(error);
    }
}

async function request(path, options = {}) {
    const config = {
        method: options.method || "GET",
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        }
    };

    if (options.body !== undefined) {
        config.body = JSON.stringify(options.body);
    }

    const response = await fetch(`${state.apiBase}${path}`, config);
    if (!response.ok) {
        const errorPayload = await parseResponse(response);
        const message = extractErrorMessage(errorPayload) || `Istek basarisiz oldu (${response.status})`;
        throw new Error(message);
    }

    if (response.status === 204) {
        return null;
    }

    return parseResponse(response);
}

async function parseResponse(response) {
    const text = await response.text();
    if (!text) {
        return null;
    }

    try {
        return JSON.parse(text);
    } catch (error) {
        return text;
    }
}

function extractErrorMessage(payload) {
    if (!payload) {
        return "";
    }
    if (typeof payload === "string") {
        return payload;
    }
    if (payload.message) {
        return payload.message;
    }
    if (payload.error) {
        return payload.error;
    }
    return "";
}

function renderAll() {
    renderStats();
    renderBrandTable();
    renderCategoryTable();
    renderCarTable();
    renderCustomerTable();
    renderRentalTable();
    renderSelectOptions();
}

function renderStats() {
    document.getElementById("brandCount").textContent = state.brands.length;
    document.getElementById("categoryCount").textContent = state.categories.length;
    document.getElementById("carCount").textContent = state.cars.length;
    document.getElementById("customerCount").textContent = state.customers.length;
    document.getElementById("rentalCount").textContent = state.rentals.length;
}

function renderBrandTable() {
    const rows = state.brands.map((brand) => `
        <tr>
            <td>${brand.id}</td>
            <td>${escapeHtml(brand.name)}</td>
            <td>${formatDateTime(brand.createdAt)}</td>
            <td class="actions">
                <button class="table-action" data-action="edit" data-id="${brand.id}">Duzenle</button>
                <button class="table-action danger" data-action="delete" data-id="${brand.id}">Sil</button>
            </td>
        </tr>
    `).join("");

    document.getElementById("brandTable").innerHTML = buildTable(
        ["ID", "Ad", "Olusturulma", "Islem"],
        rows,
        "Henuz marka kaydi yok."
    );
}

function renderCategoryTable() {
    const rows = state.categories.map((category) => `
        <tr>
            <td>${category.id}</td>
            <td>${escapeHtml(category.name)}</td>
            <td>${formatDateTime(category.createdAt)}</td>
            <td class="actions">
                <button class="table-action" data-action="edit" data-id="${category.id}">Duzenle</button>
                <button class="table-action danger" data-action="delete" data-id="${category.id}">Sil</button>
            </td>
        </tr>
    `).join("");

    document.getElementById("categoryTable").innerHTML = buildTable(
        ["ID", "Ad", "Olusturulma", "Islem"],
        rows,
        "Henuz kategori kaydi yok."
    );
}

function renderCarTable() {
    const rows = state.cars.map((car) => `
        <tr>
            <td>${car.id}</td>
            <td>${escapeHtml(car.model)}</td>
            <td>${escapeHtml(car.plateNumber)}</td>
            <td>${escapeHtml(car.brandName)}</td>
            <td>${escapeHtml(car.categoryName)}</td>
            <td>${car.year}</td>
            <td>${formatPrice(car.dailyPrice)}</td>
            <td class="actions">
                <button class="table-action" data-action="edit" data-id="${car.id}">Duzenle</button>
                <button class="table-action danger" data-action="delete" data-id="${car.id}">Sil</button>
            </td>
        </tr>
    `).join("");

    document.getElementById("carTable").innerHTML = buildTable(
        ["ID", "Model", "Plaka", "Marka", "Kategori", "Yil", "Ucret", "Islem"],
        rows,
        "Henuz arac kaydi yok."
    );
}

function renderCustomerTable() {
    const rows = state.customers.map((customer) => `
        <tr>
            <td>${customer.id}</td>
            <td>${escapeHtml(`${customer.firstName} ${customer.lastName}`)}</td>
            <td>${escapeHtml(customer.email)}</td>
            <td>${escapeHtml(customer.phoneNumber)}</td>
            <td>${escapeHtml(customer.driverLicenseNumber)}</td>
            <td class="actions">
                <button class="table-action" data-action="edit" data-id="${customer.id}">Duzenle</button>
                <button class="table-action danger" data-action="delete" data-id="${customer.id}">Sil</button>
            </td>
        </tr>
    `).join("");

    document.getElementById("customerTable").innerHTML = buildTable(
        ["ID", "Musteri", "E-posta", "Telefon", "Ehliyet", "Islem"],
        rows,
        "Henuz musteri kaydi yok."
    );
}

function renderRentalTable() {
    const rows = state.rentals.map((rental) => `
        <tr>
            <td>${rental.id}</td>
            <td>${escapeHtml(rental.customerFullName)}</td>
            <td>${escapeHtml(rental.carModel)} / ${escapeHtml(rental.carPlateNumber)}</td>
            <td>${rental.startDate}</td>
            <td>${rental.endDate}</td>
            <td>${formatPrice(rental.totalPrice)}</td>
            <td><span class="rental-status ${rental.status}">${escapeHtml(rental.status)}</span></td>
            <td class="actions">
                <button class="table-action" data-action="complete" data-id="${rental.id}">Tamamla</button>
                <button class="table-action warning" data-action="cancel" data-id="${rental.id}">Iptal et</button>
            </td>
        </tr>
    `).join("");

    document.getElementById("rentalTable").innerHTML = buildTable(
        ["ID", "Musteri", "Arac", "Baslangic", "Bitis", "Tutar", "Durum", "Islem"],
        rows,
        "Henuz kiralama kaydi yok."
    );
}

function buildTable(headers, rows, emptyMessage) {
    if (!rows) {
        return `<div class="empty-state">${emptyMessage}</div>`;
    }

    return `
        <table>
            <thead>
                <tr>${headers.map((header) => `<th>${header}</th>`).join("")}</tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

function renderSelectOptions() {
    fillSelect(
        document.getElementById("carBrandId"),
        state.brands,
        (brand) => ({ value: brand.id, label: brand.name }),
        "Marka sec"
    );
    fillSelect(
        document.getElementById("carCategoryId"),
        state.categories,
        (category) => ({ value: category.id, label: category.name }),
        "Kategori sec"
    );
    fillSelect(
        document.getElementById("rentalCustomerId"),
        state.customers,
        (customer) => ({ value: customer.id, label: `${customer.firstName} ${customer.lastName}` }),
        "Musteri sec"
    );
    fillSelect(
        document.getElementById("rentalCarId"),
        state.cars,
        (car) => ({ value: car.id, label: `${car.model} - ${car.plateNumber}` }),
        "Arac sec"
    );
}

function fillSelect(element, items, mapItem, placeholder) {
    const previousValue = element.value;
    const options = [`<option value="">${placeholder}</option>`]
        .concat(items.map((item) => {
            const mapped = mapItem(item);
            return `<option value="${mapped.value}">${escapeHtml(mapped.label)}</option>`;
        }));
    element.innerHTML = options.join("");
    if (items.some((item) => String(mapItem(item).value) === previousValue)) {
        element.value = previousValue;
    }
}

async function submitBrandForm(event) {
    event.preventDefault();
    const id = document.getElementById("brandId").value;
    const payload = {
        name: document.getElementById("brandName").value.trim()
    };
    await saveEntity("brands", id, payload, "Marka kaydedildi.");
}

async function submitCategoryForm(event) {
    event.preventDefault();
    const id = document.getElementById("categoryId").value;
    const payload = {
        name: document.getElementById("categoryName").value.trim()
    };
    await saveEntity("categories", id, payload, "Kategori kaydedildi.");
}

async function submitCarForm(event) {
    event.preventDefault();
    const id = document.getElementById("carId").value;
    const payload = {
        model: document.getElementById("carModel").value.trim(),
        plateNumber: document.getElementById("carPlateNumber").value.trim(),
        year: Number(document.getElementById("carYear").value),
        dailyPrice: Number(document.getElementById("carDailyPrice").value),
        brandId: Number(document.getElementById("carBrandId").value),
        categoryId: Number(document.getElementById("carCategoryId").value)
    };
    await saveEntity("cars", id, payload, "Arac kaydedildi.");
}

async function submitCustomerForm(event) {
    event.preventDefault();
    const id = document.getElementById("customerId").value;
    const payload = {
        firstName: document.getElementById("customerFirstName").value.trim(),
        lastName: document.getElementById("customerLastName").value.trim(),
        email: document.getElementById("customerEmail").value.trim(),
        phoneNumber: document.getElementById("customerPhoneNumber").value.trim(),
        driverLicenseNumber: document.getElementById("customerDriverLicenseNumber").value.trim()
    };
    await saveEntity("customers", id, payload, "Musteri kaydedildi.");
}

async function submitRentalForm(event) {
    event.preventDefault();
    const payload = {
        customerId: Number(document.getElementById("rentalCustomerId").value),
        carId: Number(document.getElementById("rentalCarId").value),
        startDate: document.getElementById("rentalStartDate").value,
        endDate: document.getElementById("rentalEndDate").value
    };

    try {
        await request(endpoints.rentals.create, {
            method: "POST",
            body: payload
        });
        resetRentalForm();
        await refreshAllData();
        showToast("Kiralama olusturuldu.");
    } catch (error) {
        handleError(error);
    }
}

async function saveEntity(entityKey, id, payload, successMessage) {
    try {
        const path = id ? endpoints[entityKey].update(id) : endpoints[entityKey].create;
        const method = id ? "PUT" : "POST";
        await request(path, { method, body: payload });
        formResetters[entityKey]();
        await refreshAllData();
        showToast(successMessage);
    } catch (error) {
        handleError(error);
    }
}

async function deleteEntity(entityKey, id, successMessage) {
    try {
        await request(endpoints[entityKey].delete(id), { method: "DELETE" });
        await refreshAllData();
        showToast(successMessage);
    } catch (error) {
        handleError(error);
    }
}

async function handleBrandAction(event) {
    const button = event.target.closest("button[data-action]");
    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    if (button.dataset.action === "edit") {
        const brand = state.brands.find((item) => item.id === id);
        if (!brand) {
            return;
        }
        document.getElementById("brandId").value = brand.id;
        document.getElementById("brandName").value = brand.name;
        return;
    }

    if (button.dataset.action === "delete") {
        await deleteEntity("brands", id, "Marka silindi.");
    }
}

async function handleCategoryAction(event) {
    const button = event.target.closest("button[data-action]");
    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    if (button.dataset.action === "edit") {
        const category = state.categories.find((item) => item.id === id);
        if (!category) {
            return;
        }
        document.getElementById("categoryId").value = category.id;
        document.getElementById("categoryName").value = category.name;
        return;
    }

    if (button.dataset.action === "delete") {
        await deleteEntity("categories", id, "Kategori silindi.");
    }
}

async function handleCarAction(event) {
    const button = event.target.closest("button[data-action]");
    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    if (button.dataset.action === "edit") {
        const car = state.cars.find((item) => item.id === id);
        if (!car) {
            return;
        }

        document.getElementById("carId").value = car.id;
        document.getElementById("carModel").value = car.model;
        document.getElementById("carPlateNumber").value = car.plateNumber;
        document.getElementById("carYear").value = car.year;
        document.getElementById("carDailyPrice").value = car.dailyPrice;

        const brand = state.brands.find((item) => item.name === car.brandName);
        const category = state.categories.find((item) => item.name === car.categoryName);
        document.getElementById("carBrandId").value = brand ? brand.id : "";
        document.getElementById("carCategoryId").value = category ? category.id : "";
        return;
    }

    if (button.dataset.action === "delete") {
        await deleteEntity("cars", id, "Arac silindi.");
    }
}

async function handleCustomerAction(event) {
    const button = event.target.closest("button[data-action]");
    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    if (button.dataset.action === "edit") {
        const customer = state.customers.find((item) => item.id === id);
        if (!customer) {
            return;
        }

        document.getElementById("customerId").value = customer.id;
        document.getElementById("customerFirstName").value = customer.firstName;
        document.getElementById("customerLastName").value = customer.lastName;
        document.getElementById("customerEmail").value = customer.email;
        document.getElementById("customerPhoneNumber").value = customer.phoneNumber;
        document.getElementById("customerDriverLicenseNumber").value = customer.driverLicenseNumber;
        return;
    }

    if (button.dataset.action === "delete") {
        await deleteEntity("customers", id, "Musteri silindi.");
    }
}

async function handleRentalAction(event) {
    const button = event.target.closest("button[data-action]");
    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);
    const action = button.dataset.action;
    if (action === "complete") {
        await mutateRental(endpoints.rentals.complete(id), "Kiralama tamamlandi.");
    }
    if (action === "cancel") {
        await mutateRental(endpoints.rentals.cancel(id), "Kiralama iptal edildi.");
    }
}

async function mutateRental(path, successMessage) {
    try {
        await request(path, { method: "PATCH" });
        await refreshAllData();
        showToast(successMessage);
    } catch (error) {
        handleError(error);
    }
}

function resetBrandForm() {
    document.getElementById("brandForm").reset();
    document.getElementById("brandId").value = "";
}

function resetCategoryForm() {
    document.getElementById("categoryForm").reset();
    document.getElementById("categoryId").value = "";
}

function resetCarForm() {
    document.getElementById("carForm").reset();
    document.getElementById("carId").value = "";
    renderSelectOptions();
}

function resetCustomerForm() {
    document.getElementById("customerForm").reset();
    document.getElementById("customerId").value = "";
}

function resetRentalForm() {
    document.getElementById("rentalForm").reset();
    renderSelectOptions();
    seedRentalDates();
}

function seedRentalDates() {
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);

    document.getElementById("rentalStartDate").value = formatDateInput(today);
    document.getElementById("rentalEndDate").value = formatDateInput(tomorrow);
}

function formatDateInput(date) {
    return date.toISOString().split("T")[0];
}

function formatDateTime(value) {
    if (!value) {
        return "-";
    }
    return new Intl.DateTimeFormat("tr-TR", {
        dateStyle: "short",
        timeStyle: "short"
    }).format(new Date(value));
}

function formatPrice(value) {
    if (value === undefined || value === null) {
        return "-";
    }
    return new Intl.NumberFormat("tr-TR", {
        style: "currency",
        currency: "TRY"
    }).format(Number(value));
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}

function setStatus(message, tone) {
    const badge = document.getElementById("statusBadge");
    badge.textContent = message;
    badge.className = `status-badge ${tone}`;
}

function showToast(message) {
    const toast = document.getElementById("toast");
    toast.textContent = message;
    toast.classList.remove("hidden");
    window.clearTimeout(showToast.timeoutId);
    showToast.timeoutId = window.setTimeout(() => {
        toast.classList.add("hidden");
    }, 2600);
}

function handleError(error) {
    console.error(error);
    setStatus("Baglanti hatasi", "error");
    showToast(error.message || "Beklenmeyen bir hata olustu.");
}
