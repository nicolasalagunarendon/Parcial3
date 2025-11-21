module uniquindio.edu.co.poo.elparcial3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens uniquindio.edu.co.poo.elparcial3 to javafx.fxml;
    opens uniquindio.edu.co.poo.elparcial3.ViewController to javafx.fxml;

    exports uniquindio.edu.co.poo.elparcial3;
    exports uniquindio.edu.co.poo.elparcial3.ViewController;
}