import { Item } from './item';

export interface Order {

    id: number;
    date: string;
    items: Item[];
    cost: number;

}