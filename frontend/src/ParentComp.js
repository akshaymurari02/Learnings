import React from 'react';
import ChildComp from './ChildComp';

function ParentComp() {
    const [data,setData] = React.useState("Parent Data");

    React.useEffect(()=> {
        console.log("Parent Component Rendered");
    },[]);
    
    return ( 
        <>
            {data}
            <ChildComp setData={setData} />
        </>
    );
}

export default ParentComp;