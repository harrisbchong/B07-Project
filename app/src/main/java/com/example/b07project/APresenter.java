package com.example.b07project;

public class APresenter {
    private Model model;
    private AdminLoginPage a;

    public APresenter(Model m, AdminLoginPage view) {
        this.model = m;
        this.a = view;
    }

    public void alogin(String email, String password) {

        model.adauthenticate(email, password, (Admin user) -> {
            if (user == null) {
                a.failedToLogin();
            }
            else {
               a.redirectToAdminFrontPage(user.id);
            }

        });
    }

}
