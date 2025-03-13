package lem.zona_fit.gui;


import lem.zona_fit.models.Cliente;
import lem.zona_fit.servicios.SCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

@Component
public class ClientesForm extends JFrame {
    private JPanel JpPrincipal;
    private JLabel nombreForm;
    private JPanel jpContenido;
    private JPanel jpFormulario;
    private JPanel jpBotones;
    private JTable tblClientes;
    private JScrollPane spClientes;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtMembresia;
    private JTextField txtEmail;
    private JButton btnGuardar;
    private JButton btnSalir;
    private JButton btnLimpiar;
    private JButton btnEliminar;
    private JTextField txtBuscar;
    private JPanel JpBuscar;
    private DefaultTableModel modelo;
    private final SCliente sCliente;
    private Integer idCliente ;


    @Autowired //Constructor
    public ClientesForm(SCliente sCliente){

        inicializarComponentes();
        this.sCliente = sCliente;
        inicializarTabla();
        dobleClickTabla();
        setBtnGuardar();
        setBtnLimpiar();
        setBtnEliminar();
        setBtnSalir();
        minUndMaxApp();
        validarCampos();
        setTxtBuscar();


    }

    private void listarClientes() {
        this.modelo.setRowCount(0);
        var clientes = this.sCliente.getClientes();
        clientes.forEach(cliente -> {
            Object[] row = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia(),
            };
            this.modelo.addRow(row);
        });
    }

    private void inicializarComponentes() {
        try {
            setContentPane(JpPrincipal);
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            setTitle("Clientes Zona Fit");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void minUndMaxApp(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                setLocationRelativeTo(null);
                setSize(800, 600);

            }
            @Override
            public void windowDeiconified(WindowEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);

            }

            @Override
            public void windowClosing(WindowEvent e) {
                salirApp();
            }
        });
    }

    private void inicializarTabla() {
    this.modelo = new DefaultTableModel(0,4){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    String[] cabecera= {"Id","Nombre","Apellido","Membresia"};
    this.modelo.setColumnIdentifiers(cabecera);
    this.tblClientes = new JTable(modelo);
    this.tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.spClientes = new JScrollPane(this.tblClientes);
    jpContenido.setLayout(new BorderLayout());
    this.jpContenido.add(spClientes, BorderLayout.CENTER);

    listarClientes();

}

    public void buscarClientes(){
        String busqueda = txtBuscar.getText().trim();
        this.modelo.setRowCount(0);
        if(!busqueda.isEmpty()){
            var resultados = sCliente.findByNombre(busqueda);
            resultados.forEach(cliente -> {
                Object[] row = {
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getMembresia()
                };
                this.modelo.addRow(row);
            });
        }else {
            listarClientes();
        }
    }

    private void dobleClickTabla(){
        tblClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    var row = tblClientes.getSelectedRow();
                    if (row != -1) {
                        var id = tblClientes.getValueAt(row, 0).toString();
                        idCliente = Integer.parseInt(id);
                        var nombre = tblClientes.getModel().getValueAt(row,1).toString();
                        txtNombre.setText(nombre);
                        var apellido = tblClientes.getModel().getValueAt(row,2).toString();
                        txtApellido.setText(apellido);
                        var membresia = tblClientes.getModel().getValueAt(row,3).toString();
                        txtMembresia.setText(membresia);
                    }
                    super.mouseClicked(e);
            }
            }
        });

    }

    private void salirApp(){
       int respuesta = mostrarOpcionMensaje("¿Deseas cerrar la aplicacion?",
                "Confirmar");
        if(respuesta == 0){
            System.exit(0);
        }
    }


    //Botones
    private void setBtnSalir(){
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salirApp();

            }
        });



    }
    private void setBtnLimpiar(){
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(validarCampos()) {
                   limpiarTextFields();
               }
            }
        });
    }
    private void limpiarTextFields(){
        txtNombre.setText("");
        txtApellido.setText("");
        txtMembresia.setText("");
        txtEmail.setText("");
        txtBuscar.setText("");
        txtNombre.requestFocus();
        idCliente = null;
        this.tblClientes.getSelectionModel().clearSelection();
    }
    private void setBtnEliminar(){
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarCampos()) {

                    var respuesta = mostrarOpcionMensaje("¿Esta seguro que desea eliminar el cliente?",
                            "Eliminar");
                    if (respuesta == 0) {
                        Cliente cliente = new Cliente();
                        cliente.setId(idCliente);
                        boolean resp = sCliente.deleteCliente(cliente.getId());
                        if (resp) {
                            listarClientes();
                            mostrarMensaje("Cliente eliminado exitosamente");
                            limpiarTextFields();
                        }


                    } else {
                        mostrarMensaje("El cliente no fue eliminado ");
                        limpiarTextFields();
                    }


                }
            }
        });
    }
    private void setBtnGuardar(){
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarCampos()) {
                    Cliente cliente = new Cliente();
                    cliente.setNombre(txtNombre.getText());
                    cliente.setApellido(txtApellido.getText());
                    cliente.setMembresia(Integer.valueOf(txtMembresia.getText()));



                    if (idCliente == null) { // Guardar nuevo cliente
                        var respuesta = sCliente.saveCliente(cliente);
                        if (respuesta) {
                            listarClientes();
                            mostrarMensaje("Cliente guardado exitosamente");
                            limpiarTextFields();
                        } else {
                            mostrarMensaje("Error al guardar el cliente");
                        }
                    } else { // Actualizar cliente existente
                        cliente.setId(idCliente); // Asignar idCliente
                        var respuesta = sCliente.updateCliente(cliente);
                        if (respuesta) {
                            listarClientes();
                            mostrarMensaje("Cliente actualizado exitosamente");
                            limpiarTextFields();
                        } else {
                            mostrarMensaje("Error al actualizar el cliente");
                        }
                    }
                }
            }
        });
    }
    private boolean validarCampos(){
        try{
            Integer.parseInt(txtMembresia.getText());
            return !txtNombre.getText().isEmpty()&& !txtApellido.getText().isEmpty() && !txtMembresia.getText().isEmpty();

        }catch (NumberFormatException e){
            return false;
        }

    }
    private void setTxtBuscar(){
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarClientes();
                txtApellido.setText(txtBuscar.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarClientes();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarClientes();
            }

        });
    }
    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }
    private int mostrarOpcionMensaje(String mensaje, String titulo){
        return JOptionPane.showConfirmDialog(this, mensaje, titulo, JOptionPane.YES_NO_OPTION);


    }




}
