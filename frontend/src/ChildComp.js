import React from 'react';

function ChildComp(props) {
    console.log(props);
    return ( 
        <>
            <button onClick={()=>{
                props.setData("Child Data");
            }} > update data </button>
        </>
    );
}

export default ChildComp;