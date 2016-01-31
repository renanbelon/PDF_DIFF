import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Covaciu on 20/01/2016.
 */
public class FileChooser extends Component {
    private File file;
    JFileChooser fc;

    public FileChooser() {}

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void chooseFile() {
        fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            this.setFile(selectedFile);
        } else {
            try {
                Main.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
