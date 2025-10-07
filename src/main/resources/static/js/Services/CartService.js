import axios from "https://cdn.jsdelivr.net/npm/axios@1.7.2/dist/esm/axios.min.js";
const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
export class CartService {

    async add(productId, quantity, price) {
        return await axios.post("/user/cart/add", {
            productId: productId,
            quantity: quantity,
            price: price
        }, {
            headers: {
                [header]: token
            }
        })
    }


}