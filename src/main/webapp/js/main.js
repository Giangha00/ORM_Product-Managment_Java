function confirmDelete(productName) {
    return window.confirm("Ban co chac muon xoa mem san pham \"" + productName + "\" khong?\nSan pham se khong con hien thi tren danh sach.");
}

function validateProductForm() {
    const sku = document.getElementById("sku");
    const name = document.getElementById("name");
    const price = document.getElementById("price");
    const quantity = document.getElementById("quantity");
    const categoryId = document.getElementById("categoryId");
    const warranty = document.getElementById("warrantyMonths");
    let errors = [];

    if (sku && !sku.value.trim()) {
        errors.push("SKU khong duoc de trong.");
    }
    if (!name.value.trim()) {
        errors.push("Ten san pham khong duoc de trong.");
    }
    if (price.value === "" || Number(price.value) <= 0) {
        errors.push("Gia san pham phai lon hon 0.");
    }
    if (quantity.value === "" || Number(quantity.value) < 0) {
        errors.push("So luong phai >= 0.");
    }
    if (!categoryId.value) {
        errors.push("Vui long chon danh muc.");
    }
    if (warranty && warranty.value !== "" && Number(warranty.value) < 0) {
        errors.push("Bao hanh phai >= 0.");
    }

    if (errors.length > 0) {
        alert(errors.join("\n"));
        return false;
    }
    return true;
}

document.addEventListener("DOMContentLoaded", function () {
    const successAlert = document.querySelector(".alert-success");
    if (successAlert) {
        setTimeout(function () {
            successAlert.style.display = "none";
        }, 3000);
    }
});
