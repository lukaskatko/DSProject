
public class Parser {
	
	public Integer Parse(String command) {
		int x;
		try {
			x = Integer.parseInt(command);
			if (x <= 0) {
				return 0;
			}
			if ( x > 5) {
				return 0;
			}
			return 1;
		}catch (Exception e) {
			System.out.println("Command must be an integer between 1 and 5");
			return 0;
		}
	
	}
	

}
