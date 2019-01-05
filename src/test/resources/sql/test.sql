[demo.insert]

INSERT INTO demouser (	
				name ,
				id ,
				age,
				enable
	-- 这个是注释
\\这也是注释
	// 这个同样是注释
		)
		VALUES (?,?,?,1)  
		
		
 [ demo.update]
UPDATE  demouser       

		   SET  
				name  =   #{name}      ,
				age  =   #{age}      ,
				lastUpdate=#{lastUpdate}
		WHERE 
			id = #{id}
			