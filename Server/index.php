<?php
	$json = $_GET["data"];

	$json = str_replace("[","",$json);
	$json = str_replace("]","",$json);
	
	$obj = json_decode($json);
		
	$unidad = $obj->{'unidad'};
	$valor = $obj->{'valor'};
	
	if (strcmp($unidad,"F") == 0){
		$obj->{'unidad'} = "C";
		$obj->{'valor'} = ((($valor - 32) *5) / 9);

		$json = json_encode($obj);
		//Respuesta al cliente
		echo $json;
	} else if (strcmp($unidad,"C") == 0){
		$obj->{'unidad'} = "F";
		$obj->{'valor'} = ((($valor * 9) / 5) + 32);

		$json = json_encode($obj);
		//Respuesta al cliente
		echo $json;
	} else {
		echo "Error: unidad invalida.";
	}
?>