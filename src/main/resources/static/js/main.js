import { CartController } from "./Controllers/CartController.js";
import { CartService } from "./Services/CartService.js";

const cartService = new CartService();
const cartController = new CartController(cartService);

document.addEventListener("DOMContentLoaded", function() {
    const toast = document.querySelector('.cart-toast');

    setTimeout(() => {
        toast.remove();
    }, 3000);

    cartController.init();
})



