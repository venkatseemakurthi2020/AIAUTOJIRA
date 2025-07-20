import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; // For ngModel
import { HttpClientModule } from '@angular/common/http'; // For HTTP requests

import { AppComponent } from './app.component';
import { UserStoryGeneratorComponent } from './user-story-generator/user-story-generator.component';

@NgModule({
  declarations: [
    AppComponent,
    UserStoryGeneratorComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule // Import HttpClientModule to enable HTTP services
  ],
  providers: [], // Services can be provided here or directly in the service itself (providedIn: 'root')
  bootstrap: [AppComponent]
})
export class AppModule { }