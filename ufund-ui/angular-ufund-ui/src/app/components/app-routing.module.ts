import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { AccountComponent } from './account/account.component';
import { HomeComponent } from './home/home.component';
import { SignUpComponent } from './signup/signup.component';
import { CartComponent } from './cart/cart.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { GiftDetailComponent } from './gift-detail/gift-detail.component';
import { NotfoundComponent } from './notfound/notfound.component';

const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full'},
    { path: 'home', component: HomeComponent},
    { path: 'login', component: LoginComponent},
    { path: 'account', component: AccountComponent},
    { path: 'signup', component: SignUpComponent},
    { path: 'cart', component: CartComponent},
    { path: 'admin-dashboard', component: AdminDashboardComponent},
    { path: 'detail/:id', component: GiftDetailComponent },
    { path: '**', pathMatch: 'full',  component: NotfoundComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
