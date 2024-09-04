import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class MessageService {
  messages: string[] = [];

  /**
   * Add a message to be displayed
   * @param message 
   */
  add(message: string): void {
    this.messages.push(message);
  }

  /**
   * Clear messages
   */
  clear(): void {
    this.messages = [];
  }
}
