package com.huce.edu_v2.service;

import com.huce.edu_v2.entity.InvalidatedToken;

public interface InvalidatedTokenService {

     /**
      * @param invalidatedToken - Input InvalidatedToken Object
      */
     void createInvalidatedToken(InvalidatedToken invalidatedToken);

     /**
      * @param id - Input invalidatedTokenId
      * @return boolean indicating if the id already exited or not
      */
     boolean existById(String id);
}
