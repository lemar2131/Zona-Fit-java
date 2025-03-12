package lem.zona_fit.gui;

import lem.zona_fit.models.Cliente;
import lem.zona_fit.repositorios.RCliente;
import lem.zona_fit.servicios.SCliente;
import lem.zona_fit.servicios.SClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

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
        this.sCliente = sCliente;
        inicializarComponentes();
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
       setContentPane(JpPrincipal);
       setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       setTitle("Clientes Zona Fit");
       setExtendedState(JFrame.MAXIMIZED_BOTH);
       setLocationRelativeTo(null);
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
                var row = tblClientes.getSelectedRow();
                var column = tblClientes.getSelectedColumn();
                if(row != -1 && column != -1){
                    Object value = tblClientes.getValueAt(row, column);
                    /*
                    var value1 = tblClientes.getValueAt(row, 1);
                    var value2 = tblClientes.getValueAt(row, 2);
                    var value3 = tblClientes.getValueAt(row, 3);
                    txtNombre.setText(value1.toString());
                    txtApellido.setText(value2.toString());
                    txtMembresia.setText(value3.toString());*/

                    idCliente = (Integer) tblClientes.getValueAt(row,0);
                    txtNombre.setText(sCliente.getClientes().get(row).getNombre());
                    txtApellido.setText(sCliente.getClientes().get(row).getApellido());
                    txtMembresia.setText(sCliente.getClientes().get(row).getMembresia().toString());

                }
                super.mouseClicked(e);
            }
        });

    }

    private void salirApp(){
        var respuesta = JOptionPane.showConfirmDialog(ClientesForm.this,
                "¿Deseas cerrar la aplicacion?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if(respuesta == JOptionPane.YES_OPTION){
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
                limpiarTextFields();
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
    }
    private void setBtnEliminar(){
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var respuesta = JOptionPane.showConfirmDialog(ClientesForm.this,
                        "¿Esta seguro que desea eliminar el cliente?",
                        "Eliminar",
                        JOptionPane.YES_NO_OPTION);
                if(respuesta == JOptionPane.YES_OPTION){
                    Cliente cliente = new Cliente();
                    cliente.setId(idCliente);
                   boolean resp = sCliente.deleteCliente(cliente.getId());
                   if(resp){
                       listarClientes();
                   }


                }limpiarTextFields();


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
                        System.out.println("Cliente id guardar : " + idCliente);
                        var respuesta = sCliente.saveCliente(cliente);
                        if (respuesta) {
                            listarClientes();
                            System.out.println("Cliente Guardado");
                            limpiarTextFields();
                        } else {
                            System.out.println("Error al guardar el Cliente");
                        }
                    } else { // Actualizar cliente existente
                        cliente.setId(idCliente); // Asignar idCliente
                        System.out.println("Cliente id actualizar: " + idCliente); // Verifica el valor de idCliente
                        var respuesta = sCliente.updateCliente(cliente);
                        if (respuesta) {
                            listarClientes();
                            System.out.println("Cliente Actualizado");
                            limpiarTextFields();
                        } else {
                            System.out.println("Error al actualizar Cliente");
                        }
                    }
                } else {
                    System.out.println("Campos vacíos o datos inválidos");
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




}
