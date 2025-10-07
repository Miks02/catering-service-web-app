import { CartService } from '../Services/CartService.js';

export class CartController {
    constructor(cartService = new CartService()) {
        this.cartService = cartService;
    }
    init() {
        document.querySelectorAll(".btn-add-cart").forEach(button => {
            button.addEventListener("click", async (e) => {
                e.preventDefault();

                const productId = button.dataset.productId;
                const productName = button.dataset.name;
                const price = Number(button.dataset.price);

                let quantityInput = document.getElementById("quantity");
                let quantity = 1;
                if (quantityInput) {
                    quantity = Number(quantityInput.value) || 1;
                }

                console.log("Dodajem u korpu...", productId, quantity, price);

                try {
                    await this.cartService.add(productId, quantity, price);

                    showToast({
                        type: "success",
                        title: "Dodato u korpu!",
                        message: `${productName} - ${price.toLocaleString()} RSD`
                    });

                } catch (ex) {
                    console.error(ex);
                    showToast({
                        type: "error",
                        title: "Greška pri dodavanju!",
                        message: ex?.response?.data || ex.message || "Došlo je do greške."
                    });
                }
            });

        });

        function showToast({ type = "success", title, message }) {
            const toast = document.createElement('div');
            toast.className = `cart-toast ${type}`;
            toast.innerHTML = `
                <i class="bi ${type === "success" ? "bi-check-circle-fill" : "bi-x-circle-fill"}"></i>
                <div class="toast-content">
                    <h6>${title}</h6>
                    <p>${message}</p>
                </div>
                <button class="btn-close-toast">
                    <i class="bi bi-x"></i>
                </button>
    `;

            document.body.appendChild(toast);

            toast.querySelector(".btn-close-toast").addEventListener("click", () => {
                toast.remove();
            });

            setTimeout(() => {
                toast.classList.add("hidden");
                setTimeout(() => toast.remove(), 500);
            }, 3000);
        }
    }


}
