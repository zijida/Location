package com.zijida.location.service;

import com.zijida.location.service.locationGeo;
import com.zijida.location.service.PointSet;

interface IcoreLocationService
{
    void quest_location();
    void quest_regeography();

    Location    get_location();
    PointSet    get_pointset();
    locationGeo get_geography();
}