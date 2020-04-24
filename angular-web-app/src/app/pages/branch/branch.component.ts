import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { AppModule } from '../../app.module';

@Component({
  selector: 'ngx-branch',
  templateUrl: './branch.component.html',
})
export class BranchComponent {
  notes : JSON[];
  name : String;

  constructor(private http: HttpClient, activateRoute: ActivatedRoute) {
    this.name = activateRoute.snapshot.params['name'];
    this.http.get('//localhost:8080/admin/getnotes?company='+AppModule.COMPANY
    +'&branch='+this.name).pipe().subscribe(
      (data : JSON[]) => {
        this.notes = data;
      }
    );
  }

  Done(note:JSON) {
    this.notes.splice(this.notes.indexOf(note), 1);
    this.http.get('//localhost:8080/admin/donenote?company='+AppModule.COMPANY
    +'&branch='+this.name+'&owner='+note['userName']+'&time='+note['recordingTime'])
    .pipe().subscribe();
  }
}
