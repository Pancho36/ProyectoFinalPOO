import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InterfazZapatilla extends JDialog {
    private JTextField talla;
    private JTextField material;
    private JTextField marca;
    private JTextField cantidad;
    private JTextField precio;
    private JButton ingresarButton;
    private JTextField estilo;
    private JTextField nombre;
    private JPanel contentPanel;

    public InterfazZapatilla() {
        setContentPane(contentPanel);
        setModal(true);
        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ingresarZapatilla();
                } catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void ingresarZapatilla() throws SQLException, IOException {
        Firestore db = null;
        try {
            db = FirebaseConnection.getFirestore();
            int tallaInt = Integer.parseInt(talla.getText());
            String materialStr = material.getText();
            String marcaStr = marca.getText();
            int cantidadInt = Integer.parseInt(cantidad.getText());
            int precioInt = Integer.parseInt(precio.getText());
            String estiloStr = estilo.getText();
            String nombreStr = nombre.getText();

            Zapato zapato = new Zapatilla(tallaInt, materialStr, marcaStr, cantidadInt, precioInt, null, estiloStr, nombreStr);

            DocumentReference docRef = db.collection("Zapatillas").document(zapato.getNombre());

            Map<String, Object> data = new HashMap<>();
            data.put("nombre", zapato.getNombre());
            data.put("talla", zapato.getTalla());
            data.put("material", zapato.getMaterial());
            data.put("marca", zapato.getMarca());
            data.put("cantidad", zapato.getCantidad());
            data.put("precio", zapato.getPrecio());
            data.put("estilo", ((Zapatilla) zapato).getEstilo());

            ApiFuture<WriteResult> result = docRef.set(data);
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                FirebaseConnection.close();
            }
        }
    }

    public static void main(String[] args) {
        InterfazZapatilla dialog = new InterfazZapatilla();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
