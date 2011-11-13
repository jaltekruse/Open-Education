package doc_gui.attributes;

import doc.mathobjects.AttributeException;

public class DateAttribute extends MathObjectAttribute<Date> {
	
	public DateAttribute(String n) {
		super(n);
	}

	@Override
	public Date readValueFromString(String s) throws AttributeException {
		// TODO Auto-generated method stub
		String[] monthDayYear = s.split("/");
		Date newDate = new Date();
		for ( int i = 0; i < 2 ; i++){
			if ( monthDayYear[i].length() > 2 || monthDayYear[i].length() == 0){
				throw new AttributeException("Date must be in the format mm/dd/yyyy");
			}
		}
		if ( ! (monthDayYear[2].length() == 4 || monthDayYear[2].length() == 2) ){
			throw new AttributeException("Date must be in the format mm/dd/yyyy");
		}
		try {
			newDate.setMonth(Integer.parseInt(monthDayYear[0]));
			newDate.setDay(Integer.parseInt(monthDayYear[1]));
			newDate.setYear(Integer.parseInt(monthDayYear[2]));
		}catch (AttributeException ex){
			throw ex;
		}catch (NumberFormatException ex){
			throw new AttributeException("Date must be in the format mm/dd/yyyy");
		}
		return newDate;
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return DATE;
	}

}
