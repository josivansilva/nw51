package br.com.nw51.console.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;

import javax.faces.component.UIComponent;

import org.apache.log4j.Logger;
import org.primefaces.model.TreeNode;

/**
 * Utils helper class.
 * 
 * @author Josivan Silva
 *
 */
public class Utils {
	
	static Logger logger = Logger.getLogger (Utils.class.getName());

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String NUMERATION_PATTERN = "^[0-9]+(\\.[0-9]+)*$";
	
	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;
	
	/**
	 * Gets the config folder path.
	 * 
	 * @return the config path.
	 */
	public static String getConfigPath () {
		String path = null;
		if (System.getProperty (Constants.DEV_CONFIG_PATH) != null) {
			path = getDevConfigPath ();
		} else {
			path = getDefaultConfigPath ();
		}
		return path;
	}
	
	/**
	 * Gets the default config folder path.
	 * 
	 * @return the default config path.
	 */
	private static String getDefaultConfigPath () {
		String path = System.getProperty ("user.dir");
		path += "/webapps/config/";
		return path;
	}
	
	/**
	 * Gets the dev config folder path.
	 * 
	 * @return the dev config path.
	 */
	private static String getDevConfigPath() {
		String path = System.getProperty (Constants.DEV_CONFIG_PATH) + "/";
		return path;
	}	
	
	/**
	 * Checks if a given string is not empty.
	 * 
	 * @param str the string to be checked.
	 * @return a boolean indicating the result.
	 */
	public static boolean isNonEmpty (String str) {
		boolean isNonEmpty = false;
		if (str != null && str.length() > 0) {
			isNonEmpty = true;
		}
		return isNonEmpty;
	}
	
	/**
	 * Validates a given email.
	 * 
	 * @param email the email
	 * @return a boolean indicating the result.
	 */
	public static boolean isValidEmail (String email) {
		return email.matches (EMAIL_PATTERN);
	}
	
	/**
	 * Gets the email without @domain.aaa
	 * @param email the email.
	 * @return the formatted email.
	 */
	public static String getFormattedEmail  (String email) {
		String formattedEmail = null;
		String[] tokens = email.split ("@");
		if (tokens != null && tokens.length > 0) {
			formattedEmail = tokens[0];
		}
		return formattedEmail;
	}
		
	/**
	 * Validates a date interval.
	 * 
	 * @param startDate the start date.
	 * @param endDate the end date.
	 * @return the operation result.
	 */
	public static boolean isValidDateInterval (String startDate, String endDate) {
		boolean isValid = false;
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat ("dd/MM/yyyy").parse (startDate);
		} catch (ParseException e) {
			logger.error ("An error occurred while parsing start date. " + e.getMessage());
			return isValid;
		}
		try {
			date2 = new SimpleDateFormat ("dd/MM/yyyy").parse (endDate);
		} catch (ParseException e) {
			logger.error ("An error occurred while parsing end date. " + e.getMessage());
			return isValid;
		}		
		int result = date1.compareTo (date2);
		if (result <= 0) {
			isValid = true;
		}		
		return isValid;
	}
	
	/**
	 * Removes the CPF mask.
	 * 
	 * @param cpf the CPF.
	 * @return the CPF without mask.
	 */
	public static String removeCpfMask (String cpf) {
		String newCpf = cpf.replaceAll ("\\.", "").replace ("-", "");
		return newCpf;
	}	
	
	/**
	 * Validates a CPF.
	 * 
	 * @param cpf the CPF.
	 * @return the operation result.
	 */
	public static boolean isCPF (String CPF) {
		if (CPF.equals("00000000000") 
				|| CPF.equals("11111111111") 
				|| CPF.equals("22222222222") 
				|| CPF.equals("33333333333") 
				|| CPF.equals("44444444444") 
				|| CPF.equals("55555555555") 
				|| CPF.equals("66666666666") 
				|| CPF.equals("77777777777") 
				|| CPF.equals("88888888888") 
				|| CPF.equals("99999999999") 
				|| CPF.length() != 11) {
			return false;
		}	       

		char dig10, dig11;
		int sm, i, r, num, peso;
		
		try {
		      // Calculo do 1o. Digito Verificador
		      sm = 0;
		      peso = 10;
		      for (i = 0; i < 9; i++) {              
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0         
				// (48 eh a posicao de '0' na tabela ASCII)         
		        num = (int)(CPF.charAt(i) - 48); 
		        sm = sm + (num * peso);
		        peso = peso - 1;
		      }

		      r = 11 - (sm % 11);
		      if (r == 10 || r == 11) {
		    	  dig10 = '0'; 
		      } else {
		    	  dig10 = (char)(r + 48); // converte no respectivo caractere numerico  
		      }

		      // Calculo do 2o. Digito Verificador
		      sm = 0;
		      peso = 11;
		      for ( i = 0; i < 10; i++) {
		        num = (int)(CPF.charAt(i) - 48);
		        sm = sm + (num * peso);
		        peso = peso - 1;
		      }

		      r = 11 - (sm % 11);
		      if (r == 10 || r == 11) {
		    	  dig11 = '0'; 
		      } else {
		    	  dig11 = (char)(r + 48);  
		      }

		      // Verifica se os digitos calculados conferem com os digitos informados.
		      if (dig10 == CPF.charAt(9) && dig11 == CPF.charAt (10)) {
		    	  return true; 
		      } else {
		    	  return false;
		      }
		      
		} catch (InputMismatchException e) {
		        return false;
		}
		
	}
	
	/**
	 * Formats a double value to current currency.
	 * 
	 * @param amount the amount.
	 * @return the formatted amount.
	 */
	public static String formatDoubleToReal (Double amount) {
		Locale ptBr = new Locale ("pt", "BR");
		return NumberFormat.getCurrencyInstance (ptBr).format (amount);
	}
	
	/**
	 * Formats a byte value to other representations (e.g., KB, MB, etc.).
	 * 
	 * @param value the value in bytes.
	 * @return the converted value.
	 */
	public static String convertToStringRepresentation (final long value) {
	    final long[] dividers = new long[] { T, G, M, K, 1 };
	    final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
	    if(value < 1) {
	    	throw new IllegalArgumentException("Invalid file size: " + value);
	    }	        
	    String result = null;
	    for(int i = 0; i < dividers.length; i++) {
	        final long divider = dividers[i];
	        if(value >= divider){
	            result = format (value, divider, units[i]);
	            break;
	        }
	    }
	    return result;
	}

	/**
	 * Gets the current date.
	 * 
	 * @return the current date.
	 */
	public static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date (today.getTime());
	}
		
	/**
	 * Sets the expanded mode true for all node children of a treeNode.
	 * 
	 * @param node the treeNode.
	 * @param expanded the expanded flag.
	 */
	public static void setExpandedRecursively (final TreeNode node, final boolean expanded) {
        for (final TreeNode child : node.getChildren()) {
            setExpandedRecursively (child, expanded);
        }
        node.setExpanded (expanded);
    }
	
	/**
	 * Gets a treeNode as a list.
	 * 
	 * @param node the treeNode.
	 * @param nodeList the node list.
	 * @return 
	 */
	public static List<TreeNode> getTreeNodeAsList (final TreeNode node, List<TreeNode> nodeList) {
        for (TreeNode child : node.getChildren()) {
        	nodeList.add (child);
        	if (child.getChildCount() > 0) {
        		return getTreeNodeAsList (child, nodeList);
        	}
        }
        return nodeList;
    }
	
	/**
	 * Removes a treeNode.
	 * 
	 * @param rootNode the root treeNode.
	 * @param nodeToDelete the treeNode to delete.
	 * @return the operation result.
	 */
	public static boolean removeTreeNode (TreeNode rootNode, TreeNode nodeToDelete) {
	    if (rootNode.getChildren().remove (nodeToDelete)) {
	        return true;
	    } else {
	        for (TreeNode childNode : rootNode.getChildren()) {
	            if (childNode.getChildCount() > 0) {
	                return removeTreeNode (childNode, nodeToDelete);
	            }
	        }
	        return false;
	    }
	}
	
	/**
	 * Checks if the numeration is valid or not.
	 * 
	 * @param numeration the numeration.
	 * @return the operation result.
	 */
	public static boolean isValidNumeration (String numeration) {
		if (numeration == null || numeration.length() == 0) {
			return false;
		} else {
			return numeration.matches (NUMERATION_PATTERN);	
		}
	}
	
	/**
	 * Checks if the numeration is within the allowed limit. 
	 * 
	 * @param numeration the numeration.
	 * @return the operation result.
	 */
	public static boolean isNumerationWithinLimit (String numeration) {
		if (numeration == null || numeration.length() == 0) {
			return false;
		} else {
			String[] tokens = numeration.split ("\\.");
			if (tokens.length == 1
					|| tokens.length == 2
					|| tokens.length == 3
					|| tokens.length == 4) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Formats the byte value according with its unit.
	 */
	private static String format (final long value, final long divider, final String unit) {
	    final double result = divider > 1 ? (double) value / (double) divider : (double) value;
	    return new DecimalFormat("#,##0.#").format(result) + " " + unit;
	}
	
	public static UIComponent findComponent (String id, UIComponent where) {
		if (where == null) {
		   return null;
		} else if (where.getId().equals(id)) {
			logger.debug("found - where.getId(): " + where.getId());
			logger.debug("found - where.getClientId(): " + where.getClientId());
			return where;
		} else {
		   List<UIComponent> childrenList = where.getChildren();
		   if (childrenList == null || childrenList.isEmpty()) {
		      return null;
		   }
		   for (UIComponent child : childrenList) {		      
		      UIComponent result = null;
		      result = findComponent (id, child);
		      if(result != null) {
		    	  return result;
		      }		      
		   }		   
		}
		return null;
	}
	
	public static void main (String[] args) {
		String num = "123.123.123.123.123";		
		System.out.println ("resultado: " + isNumerationWithinLimit (num));
	}
	
}
