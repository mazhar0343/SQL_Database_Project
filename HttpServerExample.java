import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.Map;
import java.io.IOException;

import java.util.Map;
import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; HttpServerExample.java
public class HttpServerExample {

    public static void main(String[] args) throws IOException {
		Database   db =  new  Database("jdbc:sqlite:Movies_Tv.db" );		

        int port = 8500;	
        HttpServer server = HttpServer.create(new InetSocketAddress(port),0);

		//Server Webpage
		
		String SELECTMovies = "SELECT * FROM Movies";		
		server.createContext("/MovieData", new RouteHandler(db,SELECTMovies));
		
		String SELECTTvshows = "SELECT * FROM Tv_Shows";		
		server.createContext("/TvData", new RouteHandler(db,SELECTTvshows));

		String SelectEverything = "SELECT Name,Tv_Shows.Year, Tv_Shows.Genres, Rating, Poster,Descripition,Tv_Shows.Cast From Tv_Shows UNION  SELECT MName, Movies.Year, Movies.Genres, MRating, MPoster, MDescription, MCast From Movies ORDER BY Tv_Shows.Year;";
		server.createContext("/HomeData", new RouteHandler(db, SelectEverything));

		String MovieFile = Input.readFile("Movies.html");
		server.createContext("/Movies", new RouteHandler(MovieFile));
		
		String TvFile = Input.readFile("TvShows.html");
		server.createContext("/Tv_Shows", new RouteHandler(TvFile));
		
		String Home = Input.readFile("Index.html");
		server.createContext("/Home", new RouteHandler(Home));

        server.start(); 
		
        System.out.println("Server is listening on port " + port );
    }    
}
