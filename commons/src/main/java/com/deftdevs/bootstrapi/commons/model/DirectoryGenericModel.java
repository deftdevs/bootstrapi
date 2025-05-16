package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model for all user directories that are not supported by the plugin(s) yet.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = BootstrAPI.DIRECTORY + '-' + BootstrAPI.DIRECTORY_GENERIC)
public class DirectoryGenericModel extends AbstractDirectoryModel {

}
