import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Covaciu on 20/01/2016.
 */
public class SpellChecking extends Component {
    private File file;
    JFileChooser fc;

    public SpellChecking() {

    }

    public SpellChecking(File file) {
        this.setFile(file);
    }

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
            chooseFile();
        }
    }

}
