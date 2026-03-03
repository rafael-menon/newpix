package newpixclient.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CpfFilter extends DocumentFilter {
	final private char dot = '.';
	final private char dash = '-';
	final private int cpfLength = 14;
	
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.setLength(cpfLength);
        sb.setCharAt(3, dot);
        sb.setCharAt(7, dot);
        sb.setCharAt(11, dash);
        if ((fb.getDocument().getLength() + string.length()) <= cpfLength && string.length() < 2) {
        	switch(fb.getDocument().getLength()) {
        	case 3, 7:
        		sb.append(dot);
        		break;
        	case 11:
        		sb.append(dash);
        		break;
        	}
        	
            for (char c : string.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
        }
        
        super.insertString(fb, offset, sb.toString(), attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
        if (string == null) {
            super.replace(fb, offset, length, string, attrs);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        
        if ((fb.getDocument().getLength() - length + string.length()) <= cpfLength && string.length() < 2) {   	
        	switch(fb.getDocument().getLength()) {
        	case 3, 7:
        		sb.append(dot);
        		break;
        	case 11:
        		sb.append(dash);
        		break;
        	}

            for (char c : string.toCharArray()) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
        		
        }
        
        
        super.replace(fb, offset, length, sb.toString(), attrs);
    }
}

