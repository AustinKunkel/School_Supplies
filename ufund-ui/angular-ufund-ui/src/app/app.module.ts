import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './components/app-routing.module';
import { AppComponent } from './components/app.component';

import { HttpClientModule} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NotfoundComponent } from './components/notfound/notfound.component';

@NgModule({
    declarations: [
      AppComponent,
      NotfoundComponent,
    ],
    imports: [
      BrowserModule,
      AppRoutingModule,
      HttpClientModule,
      FormsModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }
