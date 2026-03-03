package newpixserver.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PortFilter extends DocumentFilter {
	final private int portLength = 5;
	
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        if ((fb.getDocument().getLength() + string.length()) <= portLength) {
            for (char c : string.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
        }
        
        super.insertString(fb, offset, sb.toString(), attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) {
            super.replace(fb, offset, length, text, attrs);
            return;
        }
 
        StringBuilder sb = new StringBuilder();
        if ((fb.getDocument().getLength() - length + text.length()) <= portLength) {
            for (char c : text.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            
        }
        
        super.replace(fb, offset, length, sb.toString(), attrs);
    }
}
