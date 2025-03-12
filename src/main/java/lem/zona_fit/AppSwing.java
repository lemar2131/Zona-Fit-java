package lem.zona_fit;


import lem.zona_fit.gui.ClientesForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
@SpringBootApplication
public class AppSwing {

    public static void main(String[] args) {
        //instanciarl la fabrica de Spring

        ConfigurableApplicationContext contex = new SpringApplicationBuilder(AppSwing.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);

        //crear el objeto swing
        SwingUtilities.invokeLater(() ->{
            ClientesForm clientesForm = contex.getBean(ClientesForm.class);
            clientesForm.setVisible(true);

        });

    }


}
