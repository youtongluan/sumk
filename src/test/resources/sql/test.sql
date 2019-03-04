[demo.insert]

INSERT INTO demo_user (	
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
UPDATE  demo_user       

		   SET  
				name  =   #{name}      ,
				age  =   #{age}      ,
				last_update=#{lastUpdate}
		WHERE 
			id = #{id}
			