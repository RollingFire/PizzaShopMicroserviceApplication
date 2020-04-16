import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderUserSectionComponent } from './header-user-section.component';

describe('HeaderUserSectionComponent', () => {
  let component: HeaderUserSectionComponent;
  let fixture: ComponentFixture<HeaderUserSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeaderUserSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderUserSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
