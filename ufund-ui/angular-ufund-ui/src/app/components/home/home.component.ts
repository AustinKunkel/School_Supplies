import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { GiftsComponent } from '../gifts/gifts.component';

import { CurrentUserService } from '../../services/current.user.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-home',
  imports: [
    GiftsComponent,
    NavbarComponent,
    CommonModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  name : string | undefined;

  constructor(private currUserService : CurrentUserService){}
  
  

  ngOnInit() {
    if(this.currUserService.isUserLoggedIn()) {
      this.name = this.currUserService.getCurrentUser().username;
    }
  }
}
