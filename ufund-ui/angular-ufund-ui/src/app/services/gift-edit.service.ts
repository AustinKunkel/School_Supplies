import { Injectable } from '@angular/core';
import { Gift } from '../model/gift';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

/**
 * As services are singleton and only one of it is instantiated
 * Can only support one client at a time
 * 
 * Needs to be fixed (Sprint 3)
 */
@Injectable({ providedIn: 'root' })
export class GiftEditService {
  
  private baseGift: Gift = {
    //the base gift that will be changed eventually so it can be used in the html component
    id: -1, // uninitialized 
    name: '',
    description: '',
    price: 0,
    priority: 'NONE',
    amount_needed: 0
  };

  currentGift: BehaviorSubject<Gift> = new BehaviorSubject<Gift>(this.baseGift);

  /**
   * Set the current gift of the website
   * @param gift 
   */
  setCurrentGift(gift: Gift): void {
    this.currentGift.next(gift);
  }

  /**
   * Get the current gift
   * @returns current gift 
   */
  getCurrentGift(): Gift {
    return this.currentGift.getValue();
  }

  /**
   * Clear the current gift
   */
  clearCurrentGift(): void {
    this.currentGift.next(this.baseGift);
  }

  /**
   * Get the base gift
   * @returns base gift 
   */
  getBaseGift(): Gift {
    return this.baseGift;
  }

}