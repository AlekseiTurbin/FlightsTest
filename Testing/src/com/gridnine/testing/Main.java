package com.gridnine.testing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	
	
     public static void main(String[] args) {
        	// TODO Auto-generated method stub
    	 
                FlightBuilder flightBuilder = new FlightBuilder();
                
                System.out.println("Все перелеты тестового набора:");
                System.out.println(FlightBuilder.createFlights().toString());
                System.out.println();
                
                String str = FlightBuilder.createFlights().toString(); // Переводим в формат String
                str = str.substring(1, str.length() - 1); // Удаляем первый и последний символы (квадратные скобки созданного массива данных перелетов)
                
                // Конвертируем формат
                Pattern pattern = Pattern.compile("(?<=\\[).*?(?=\\])");
//                Matcher matcher = pattern.matcher(str);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime currentTime = LocalDateTime.now(); // Получаем текущую дату
                
                System.out.printf("1. Вылет(ы) до текущего момента времени (%s):%n", dtf.format(currentTime).toString());
                departureUntilCurrentTime(pattern.matcher(str), currentTime);
                System.out.println();
                
                System.out.println("2. Имеются сегменты с датой прилёта раньше даты вылета:");
                arrivalDateBeforeDepartureDate(pattern.matcher(str));
                System.out.println();
                
                System.out.println("3. Общее время, проведённое на земле превышает два часа (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним):");
                timeExceedsTwoHours(pattern.matcher(str));

        }

     
     public static void departureUntilCurrentTime(Matcher matcher, LocalDateTime currentTime) {
    	int count = 0;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		
  		while(matcher.find()){
  			String departure = matcher.group().substring(0, matcher.group().indexOf("|"));
  			LocalDateTime departureTime = LocalDateTime.parse(departure, dtf);
  			
  			if (departureTime.isBefore(currentTime)) {
  				System.out.println(matcher.group());
  				count = ++count;
  			}
         }
  		
  		if (count == 0) System.out.println("< Нет перелетов по заданным параметрам >");
  	}
  	

  	public static void arrivalDateBeforeDepartureDate(Matcher matcher) {
  		int count = 0;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		
  		while(matcher.find()){
  			String departure = matcher.group().substring(0, matcher.group().indexOf("|"));
  			String arrival = matcher.group().substring(matcher.group().indexOf("|") + 1);
  			LocalDateTime departureTime = LocalDateTime.parse(departure, dtf);
  			LocalDateTime arrivalTime = LocalDateTime.parse(arrival, dtf);
  			
  			if (arrivalTime.isBefore(departureTime)) {
  				System.out.println(matcher.group());
  				count = ++count;
  			}
         }
  		
  		if (count == 0) System.out.println("< Нет перелетов по заданным параметрам >");
  	}
  	

  	public static void timeExceedsTwoHours(Matcher matcher) {
  		int count = 0;
  		String previousArrive = "";
  		String previousFlight = "";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		
  		while(matcher.find()){

  			if (!previousArrive.equals("")) {
  	  			LocalDateTime arrivalTimePreviousFlight = LocalDateTime.parse(previousArrive, dtf);
  	  			
  				String departure = matcher.group().substring(0, matcher.group().indexOf("|"));
  	  			LocalDateTime departureTime = LocalDateTime.parse(departure, dtf);

  	  			long hours = ChronoUnit.MINUTES.between(arrivalTimePreviousFlight, departureTime);
  	  			
  	  			if (hours > 120) {
  	  				System.out.println(previousFlight + " - " + matcher.group() + " (" + hours/60 + ")");
  	  				count = ++count;
  	  			}
  			}
  			
  			previousArrive = matcher.group().substring(matcher.group().indexOf("|") + 1);
  			previousFlight = matcher.group();
         }
  		
  		if (count == 0) System.out.println("< Нет перелетов по заданным параметрам >");
  	}



}

