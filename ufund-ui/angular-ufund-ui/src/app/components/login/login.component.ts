import { Component, ElementRef, ViewChild } from '@angular/core';

import { UserService } from "../../services/user.service";
import { CurrentUserService } from '../../services/current.user.service';
import { Router, RouterLink } from '@angular/router';

import { User } from '../../model/user';
import { FormsModule } from '@angular/forms';
import { Location, NgIf } from '@angular/common';

import { sha512 } from 'sha512-crypt-ts';

import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [
    FormsModule,
    RouterLink,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  errorMessage: SafeHtml | undefined;

  constructor(
    private currentUserService: CurrentUserService,
    private userService: UserService,
    private router: Router,
    private location: Location,
    private sanitizer : DomSanitizer) { }

  userInfo: User = this.currentUserService.getCurrentUser();


  
  /**
   * Sign in to the user account
   * @param user 
   */
  signIn(user: User): void {
    const inputUser = user;

    if (this.checkIfEmailValid(user.email)) {
      this.userService.getUser(inputUser.email).subscribe( // sends request to get user associated with given mail
        (userResponse: User) => {
          console.log('User data: ', userResponse);

          const fetchedUser: User = userResponse; // creates a user from server response

          if (fetchedUser) {
            const password_hash: string = sha512.hex(inputUser.password);
            console.log(password_hash);
            console.log(fetchedUser.password);

            if (password_hash === fetchedUser.password) {

              this.currentUserService.setCurrentUser(fetchedUser);

              if (this.currentUserService.isAdmin())
                this.router.navigate(['/admin-dashboard'])
              else 
                this.router.navigate(['']);
            }

            else {
              this.errorMessage = this.sanitizer.bypassSecurityTrustHtml('Invalid password, <span class="link" id="forgotPassLink">forgot password?</span>');
            }
          }
        }
      );
    } 
    
    else
      this.errorMessage = this.sanitizer.bypassSecurityTrustHtml('Invalid email, try again or <span class="link" id="signupLink">sign up</span>')
  }

  /**
   * Check if an email is valid
   * @param email 
   * @returns true if the email is a valid email address
   *          false if not
   */
  checkIfEmailValid(email: string) : boolean{
    const res = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;

    return res.test(String(email).toLowerCase());
  }

  /**
   * Check if a user is an admin
   * @param user 
   * @returns true if the user is the admin
   */
  isAdmin(user: User): boolean {
    // TODO - update getting of admin email through REST API call instead
    return (user.email == 'admin@google.com');
  }

  /**
   * Change view to user's previous route
   */
  goBack(): void {
    this.location.back();
  }

  navToForgotPass() : void{
    this.router.navigate(['']);
  }

  navToSignUp() {
    this.router.navigate(['/signup']);
  }

  onContainerClick(event: Event) {
    if ((event.target as Element).id === 'forgotPassLink') {
      this.navToForgotPass();
    } else if ((event.target as Element).id === 'signupLink') {
      this.navToSignUp();
    }
  }
}

