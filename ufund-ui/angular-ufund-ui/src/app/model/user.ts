import { Item } from './item';
import { Order } from './order';

export interface User {
  
    id: number;
    username: string;
    password: string;
    email: string;
    cart: Item[];
    orders: Order[];

}
  