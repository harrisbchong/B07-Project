package com.example.b07project;

public class SPresenter {
    private Model model;
    private StudentLoginPage s;

    public SPresenter(Model m, StudentLoginPage view) {
        this.model = m;
        this.s = view;
    }
    public void stulogin(String email, String password) {

        model.stuauthenticate(email, password, (Student user) -> {
            if (user == null) {
                s.failedToLogin();
            }
            else {
                s.redirectToStudentFrontPage(user.id);
            }
        });
    }


}
