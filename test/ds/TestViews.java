package ds;

import javax.swing.JFrame;

import com.mebigfatguy.ds.DSException;
import com.mebigfatguy.ds.DSFactory;
import com.mebigfatguy.ds.DSTypeReference;

public class TestViews {

    public static void main(String[] args) throws DSException {
        
        JFrame f = DSFactory.<JFrame>getView("/sample1.xml", new DSTypeReference<JFrame>() {}, new TestLocalizer());
        
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
