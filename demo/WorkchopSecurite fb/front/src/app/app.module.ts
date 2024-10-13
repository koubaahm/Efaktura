import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthIntercepterService } from './services/auth-intercepter.service';
import { RegisterComponent } from './pages/register/register.component';
import { FormsModule } from '@angular/forms';
import { NgxBootstrapMultiselectModule } from 'ngx-bootstrap-multiselect';
import { LoginComponent } from './pages/login/login.component';
import { MultiSelectModule } from 'primeng/multiselect';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgxBootstrapMultiselectModule,
    MultiSelectModule,
    BrowserAnimationsModule
  ],
  providers: [
    {provide :HTTP_INTERCEPTORS, useClass:AuthIntercepterService,multi :true} 
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
