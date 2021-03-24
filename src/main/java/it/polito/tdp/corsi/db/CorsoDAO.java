package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;

public class CorsoDAO {
	
	public List<Corso>getCorsiByPeriodo(Integer periodo)
	{
		String sql="SELECT * " 
				+ "FROM corso "
				+ "WHERE pd=? ";
		List<Corso> result=new ArrayList<Corso>();
		
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setInt(1, periodo);
			//vuol dire che imposto il valore periodo al parametro 1
			//che poi sarebbe l'unico nella nostra query
			ResultSet rs=st.executeQuery();
			
			while(rs.next())
			{
				Corso c=new Corso(rs.getString("codins"),rs.getInt("crediti"),rs.getString("nome"),rs.getInt("pd"));
				result.add(c);
			}
			conn.close();
			rs.close();
			st.close();
			
			}catch(SQLException e)
			{
				throw new RuntimeException(e);
			}
		return result;
	}
	
	//ha senso usare una mappa con chiave corso e valore numero di iscritti
	//si potrebbe anche fare una nuova classe ma essendo che cambiano così poco 
	//ha senso usare la map
	
	public Map<Corso,Integer> getIscrittiByPeriodo(Integer periodo)
	{

		String sql="SELECT c.codins,c.nome,c.crediti,c.pd,COUNT(*)AS tot "
				+ "FROM corso c, iscrizione i "
				+ "WHERE c.codins=i.codins AND c.pd=? "
				+ "GROUP BY c.codins,c.nome,c.crediti,c.pd "
				+ "ORDER BY tot ";
		Map<Corso,Integer> result=new HashMap<Corso,Integer>();
		
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setInt(1, periodo);
			//vuol dire che imposto il valore periodo al parametro 1
			//che poi sarebbe l'unico nella nostra query
			ResultSet rs=st.executeQuery();
			
			while(rs.next())
			{
				Corso c=new Corso(rs.getString("codins"),rs.getInt("crediti"),rs.getString("nome"),rs.getInt("pd"));
				Integer n=rs.getInt("tot");
				result.put(c,n);
			}
			conn.close();
			rs.close();
			st.close();
			
			}catch(SQLException e)
			{
				throw new RuntimeException(e);
			}
		return result;
	}
	
	

}
