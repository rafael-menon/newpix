package newpixclient.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class IpFilter extends DocumentFilter {
	final private char dot = '.';
	final private int ipLength = 15;
	
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        if ((fb.getDocument().getLength() + string.length()) <= ipLength) {
            for (char c : string.toCharArray()) {
                if (Character.isDigit(c) || c == dot) {
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
        if ((fb.getDocument().getLength() - length + text.length()) <= ipLength) {
            for (char c : text.toCharArray()) {
                if (Character.isDigit(c) || c == dot) {
                    sb.append(c);
                }
            }
            
        }
        
        super.replace(fb, offset, length, sb.toString(), attrs);
    }
}